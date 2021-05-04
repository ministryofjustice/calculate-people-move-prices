package uk.gov.justice.digital.hmpps.pecs.jpc.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.hmpps.pecs.jpc.location.Location
import uk.gov.justice.digital.hmpps.pecs.jpc.location.LocationRepository
import uk.gov.justice.digital.hmpps.pecs.jpc.location.LocationType
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Money
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Price
import uk.gov.justice.digital.hmpps.pecs.jpc.price.PriceRepository
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Supplier
import uk.gov.justice.digital.hmpps.pecs.jpc.price.effectiveYearForDate
import java.time.LocalDate

@ExtendWith(FakeAuthentication::class)
internal class SupplierPricingServiceTest {

  private val auditService: AuditService = mock()
  private val effectiveYear = effectiveYearForDate(LocalDate.now())
  private val locationRepository: LocationRepository = mock()
  private val fromLocation: Location = Location(locationType = LocationType.PR, nomisAgencyId = "PRISON", siteName = "from site")
  private val toLocation: Location = Location(locationType = LocationType.MC, nomisAgencyId = "COURT", siteName = "to site")
  private val priceRepository: PriceRepository = mock()
  private val price: Price = Price(supplier = Supplier.SERCO, priceInPence = 10024, fromLocation = fromLocation, toLocation = toLocation, effectiveYear = effectiveYear)
  private val priceCaptor = argumentCaptor<Price>()
  private val service: SupplierPricingService = SupplierPricingService(locationRepository, priceRepository, auditService)

  @Test
  internal fun `site names returned for new pricing`() {
    whenever(locationRepository.findByNomisAgencyId("FROM")).thenReturn(fromLocation)
    whenever(locationRepository.findByNomisAgencyId("TO")).thenReturn(toLocation)
    whenever(
      priceRepository.findBySupplierAndFromLocationAndToLocation(
        Supplier.SERCO,
        fromLocation,
        toLocation
      )
    ).thenReturn(null)

    val result = service.getSiteNamesForPricing(Supplier.SERCO, "from", "to", effectiveYear)

    assertThat(result).isEqualTo(Pair("from site", "to site"))
    verify(locationRepository).findByNomisAgencyId("FROM")
    verify(locationRepository).findByNomisAgencyId("TO")
    verify(priceRepository).findBySupplierAndFromLocationAndToLocationAndEffectiveYear(
      Supplier.SERCO,
      fromLocation,
      toLocation,
      effectiveYear
    )
  }

  @Test
  internal fun `add new price for supplier`() {
    whenever(locationRepository.findByNomisAgencyId("FROM")).thenReturn(fromLocation)
    whenever(locationRepository.findByNomisAgencyId("TO")).thenReturn(toLocation)
    whenever(priceRepository.save(any())).thenReturn(price)

    service.addPriceForSupplier(
      Supplier.SERCO,
      "from",
      "to",
      Money.valueOf(100.24),
      effectiveYear
    )

    verify(locationRepository).findByNomisAgencyId("FROM")
    verify(locationRepository).findByNomisAgencyId("TO")
    verify(priceRepository).save(priceCaptor.capture())

    assertThat(priceCaptor.firstValue.priceInPence).isEqualTo(10024)
  }

  @Test
  internal fun `existing site names and price returned`() {
    whenever(locationRepository.findByNomisAgencyId("FROM")).thenReturn(fromLocation)
    whenever(locationRepository.findByNomisAgencyId("TO")).thenReturn(toLocation)
    whenever(
      priceRepository.findBySupplierAndFromLocationAndToLocation(
        Supplier.SERCO,
        fromLocation,
        toLocation
      )
    ).thenReturn(price)

    val result = service.getExistingSiteNamesAndPrice(Supplier.SERCO, "from", "to", effectiveYear)

    assertThat(result).isEqualTo(Triple("from site", "to site", Money.valueOf(100.24)))
    verify(locationRepository).findByNomisAgencyId("FROM")
    verify(locationRepository).findByNomisAgencyId("TO")
    verify(priceRepository).findBySupplierAndFromLocationAndToLocation(Supplier.SERCO, fromLocation, toLocation)
  }

  @Test
  internal fun `attempt to update existing price to same price has no effect`() {
    whenever(locationRepository.findByNomisAgencyId("FROM")).thenReturn(fromLocation)
    whenever(locationRepository.findByNomisAgencyId("TO")).thenReturn(toLocation)
    whenever(
      priceRepository.findBySupplierAndFromLocationAndToLocationAndEffectiveYear
      (
        Supplier.SERCO,
        fromLocation,
        toLocation,
        2020
      )
    ).thenReturn(price)
    whenever(priceRepository.save(any())).thenReturn(price)

    service.updatePriceForSupplier(
      Supplier.SERCO,
      "from",
      "to",
      price.price(),
      effectiveYear
    )

    verify(priceRepository, never()).save(any())
    verify(auditService, never()).create(any())
  }

  @Test
  internal fun `update existing price for supplier`() {
    whenever(locationRepository.findByNomisAgencyId("FROM")).thenReturn(fromLocation)
    whenever(locationRepository.findByNomisAgencyId("TO")).thenReturn(toLocation)
    whenever(
      priceRepository.findBySupplierAndFromLocationAndToLocationAndEffectiveYear
      (
        Supplier.SERCO,
        fromLocation,
        toLocation,
        2020
      )
    ).thenReturn(price)
    whenever(priceRepository.save(any())).thenReturn(price)

    service.updatePriceForSupplier(
      Supplier.SERCO,
      "from",
      "to",
      Money.Factory.valueOf(200.35),
      effectiveYear
    )

    verify(locationRepository).findByNomisAgencyId("FROM")
    verify(locationRepository).findByNomisAgencyId("TO")
    verify(priceRepository).findBySupplierAndFromLocationAndToLocationAndEffectiveYear(
      Supplier.SERCO,
      fromLocation,
      toLocation,
      effectiveYear
    )
    verify(priceRepository).save(price)
    assertThat(price.priceInPence).isEqualTo(20035)
  }
}
