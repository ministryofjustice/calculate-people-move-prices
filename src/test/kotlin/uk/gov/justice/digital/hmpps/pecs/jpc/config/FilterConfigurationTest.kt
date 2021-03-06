package uk.gov.justice.digital.hmpps.pecs.jpc.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.pecs.jpc.controller.HtmlController
import uk.gov.justice.digital.hmpps.pecs.jpc.controller.MaintainSupplierPricingController
import uk.gov.justice.digital.hmpps.pecs.jpc.controller.ManageSchedule34LocationsController
import uk.gov.justice.digital.hmpps.pecs.jpc.controller.MapFriendlyLocationController

class FilterConfigurationTest {

  @Test
  fun `choose supplier URLs filtered`() {
    assertThat(FilterConfiguration().chooseFilter().urlPatterns).containsExactlyInAnyOrder(
      *HtmlController.routes(),
      *MaintainSupplierPricingController.routes(),
      *ManageSchedule34LocationsController.routes(),
      *MapFriendlyLocationController.routes()
    )
  }
}
