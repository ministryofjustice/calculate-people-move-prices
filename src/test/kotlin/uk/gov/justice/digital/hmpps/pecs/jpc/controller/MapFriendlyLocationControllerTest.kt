package uk.gov.justice.digital.hmpps.pecs.jpc.controller

import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import uk.gov.justice.digital.hmpps.pecs.jpc.TestConfig
import uk.gov.justice.digital.hmpps.pecs.jpc.location.LocationType
import uk.gov.justice.digital.hmpps.pecs.jpc.service.MapFriendlyLocationService

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = [TestConfig::class])
internal class MapFriendlyLocationControllerTest(@Autowired private val wac: WebApplicationContext) {

  private val mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()

  private val agencyId = "123456"

  @MockBean
  lateinit var service: MapFriendlyLocationService

  @Test
  internal fun `get map location`() {
    mockMvc.get("/map-location/${agencyId}")
            .andExpect { model { attribute("form", MapFriendlyLocationController.MapLocationForm(agencyId)) } }
            .andExpect { view { name("map-location") } }
            .andExpect { status { isOk } }
  }

  @Test
  internal fun `post map location successful`() {
    mockMvc.post("/map-location") {
      param("agencyId", "123456")
      param("locationName", "Friendly Location Name")
      param("locationType", "CC")
    }
            .andExpect { model { hasNoErrors() } }
            .andExpect { status { is3xxRedirection } }
            .andExpect { redirectedUrl("/dashboard") }

    verify(service).mapFriendlyLocation(agencyId, "Friendly Location Name", LocationType.CC)
  }

  @Test
  internal fun `post map location fails`() {
    mockMvc.post("/map-location") {
      param("agencyId", "123456")
      param("locationName", "")
      param("locationType", "CC")
    }
            .andExpect { model { attributeHasFieldErrorCode("form", "locationName", "NotEmpty") } }
            .andExpect { view { name("/map-location") } }
            .andExpect { status { isOk } }
  }
}
