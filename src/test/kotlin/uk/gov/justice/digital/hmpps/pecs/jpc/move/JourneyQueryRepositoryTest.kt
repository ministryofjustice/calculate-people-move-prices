package uk.gov.justice.digital.hmpps.pecs.jpc.move

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.pecs.jpc.TestConfig
import uk.gov.justice.digital.hmpps.pecs.jpc.location.Location
import uk.gov.justice.digital.hmpps.pecs.jpc.location.LocationRepository
import uk.gov.justice.digital.hmpps.pecs.jpc.location.LocationType
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Price
import uk.gov.justice.digital.hmpps.pecs.jpc.price.PriceRepository
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Supplier
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.report.GNICourtLocation
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.report.WYIPrisonLocation
import java.util.UUID

@ActiveProfiles("test")
@DataJpaTest
@Import(TestConfig::class)
internal class JourneyQueryRepositoryTest {

    @Autowired
    lateinit var locationRepository: LocationRepository

    @Autowired
    lateinit var priceRepository: PriceRepository

    @Autowired
    lateinit var moveRepository: MoveRepository

    @Autowired
    lateinit var journeyQueryRepository: JourneyQueryRepository

    @Autowired
    lateinit var journeyRepository: JourneyRepository


    @Autowired
    lateinit var entityManager: TestEntityManager

    val wyi = WYIPrisonLocation()
    val gni = GNICourtLocation()

    val standardMove = move( dropOffOrCancelledDateTime = moveDate.atStartOfDay().plusHours(5)) // should appear before the one above
    val journeyModel1 = journey()
    val journeyModel2 = journey(journeyId = "J2")

    @BeforeEach
    fun beforeEach(){
        locationRepository.save(wyi)
        locationRepository.save(gni)
        priceRepository.save(Price(id = UUID.randomUUID(), fromLocation = wyi, toLocation = gni, priceInPence = 999, supplier = Supplier.SERCO))

        moveRepository.save(standardMove)
        journeyRepository.save(journeyModel1)
        journeyRepository.save(journeyModel2)

        entityManager.flush()
    }




    @Test
    fun `unique journeys and journey summaries`() {

        val locationX  = Location(id = UUID.randomUUID(), locationType = LocationType.CO, nomisAgencyId = "locationX", siteName = "banana")
        val locationY  = Location(id = UUID.randomUUID(), locationType = LocationType.CO, nomisAgencyId = "locationY", siteName = "apple")

        locationRepository.save(locationX)
        locationRepository.save(locationY)

        priceRepository.save(Price(id = UUID.randomUUID(), fromLocation = wyi, toLocation = locationX, priceInPence = 201, supplier = Supplier.SERCO))

        val moveWithUnbillableJourney = standardMove.copy(moveId = "M2")
        val journey3 = journey(moveId = "M2", journeyId = "J3", billable = false, toNomisAgencyId = locationX.nomisAgencyId)

        val moveWithUnmappedLocation = standardMove.copy(moveId = "M3")
        val journey4 = journey(moveId = "M3", journeyId = "J4", billable = true, fromNomisAgencyId = "unmappedNomisAgencyId")

        val moveWithUpricedLocation = standardMove.copy(moveId = "M4")
        val journey5 = journey(moveId = "M4", journeyId = "J5", billable = true, fromNomisAgencyId = locationY.nomisAgencyId)

        moveRepository.save(moveWithUnbillableJourney) // not unpriced just because journey is not billable
        moveRepository.save(moveWithUpricedLocation) // unpriced but has location
        moveRepository.save(moveWithUnmappedLocation) // unpriced since it has no mapped location

        journeyRepository.save(journey3)
        journeyRepository.save(journey4)
        journeyRepository.save(journey5)

        entityManager.flush()

        val summaries = journeyQueryRepository.journeysSummaryInDateRange(Supplier.SERCO, moveDate, moveDate)
        assertThat(summaries).isEqualTo(JourneysSummary(4, 1998, 1, 2, Supplier.SERCO))

        val unpricedUniqueJourneys = journeyQueryRepository.distinctJourneysAndPriceInDateRange(Supplier.SERCO, moveDate, moveDate)
        assertThat(unpricedUniqueJourneys.size).isEqualTo(2)

        // Ordered by unmapped from locations first
        assertThat(unpricedUniqueJourneys[0].fromNomisAgencyId).isEqualTo("unmappedNomisAgencyId")
    }

    @Test
    fun `distinct journeys filtered on from location with empty to location`() {
        val journey2 = journey(moveId = "M1", journeyId = "J2", toNomisAgencyId = "NEW")
        journeyRepository.save(journey2)
        entityManager.flush()

        val journeys = journeyQueryRepository.distinctJourneysBySiteNames(Supplier.SERCO, "from", "")
        assertThat(journeys).containsExactlyInAnyOrder(
                DistinctJourney(fromNomisAgencyId="WYI", LocationType.PR, fromSiteName="from", toNomisAgencyId="GNI", LocationType.CO, toSiteName="to"),
                DistinctJourney(fromNomisAgencyId="WYI", LocationType.PR, fromSiteName="from", toNomisAgencyId="NEW", null, toSiteName=null)
        )
    }

    @Test
    fun `distinct journeys filtered on to location with whitespace from location`() {
        val journey2 = journey(moveId = "M1", journeyId = "J2", fromNomisAgencyId = "NEW", toNomisAgencyId = "GNI")
        journeyRepository.save(journey2)
        entityManager.flush()

        val journeys = journeyQueryRepository.distinctJourneysBySiteNames(Supplier.SERCO, " ", "to")
        assertThat(journeys).containsExactlyInAnyOrder(
                DistinctJourney(fromNomisAgencyId="WYI", LocationType.PR, fromSiteName="from", toNomisAgencyId="GNI", LocationType.CO, toSiteName="to"),
                DistinctJourney(fromNomisAgencyId="NEW", null, fromSiteName=null, toNomisAgencyId="GNI", LocationType.CO, toSiteName="to")
        )
    }

}