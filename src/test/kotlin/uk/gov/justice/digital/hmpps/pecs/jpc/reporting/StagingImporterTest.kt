package uk.gov.justice.digital.hmpps.pecs.jpc.reporting

import org.junit.Ignore
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.pecs.jpc.pricing.Supplier

import java.time.LocalDate

@ActiveProfiles("dev")
//@SpringBootTest
internal class StagingImporterTest {

    @Autowired
    lateinit var importer: ReportingImporter

    fun `Standard moves should only include completed moves with one billable journey`() {
        val allMoves = importer.import(LocalDate.of(2020, 9, 24), 1)
        val standardMoves = MoveFilterer.standardMoves(Supplier.GEOAMEY, allMoves)
    }

}
