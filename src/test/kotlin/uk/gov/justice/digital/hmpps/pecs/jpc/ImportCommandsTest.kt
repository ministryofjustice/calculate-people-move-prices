package uk.gov.justice.digital.hmpps.pecs.jpc

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.pecs.jpc.commands.ImportCommands
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.ManualLocationImporter
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.ManualPriceImporter
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.ReportsImporter
import java.time.LocalDate

internal class ImportCommandsTest {

  private val locationImporter: ManualLocationImporter = mock()
  private val manualPriceImporter: ManualPriceImporter = mock()
  private val reportsImporter: ReportsImporter = mock()
  private val date: LocalDate = LocalDate.EPOCH

  private val commands: ImportCommands = ImportCommands(locationImporter, manualPriceImporter, reportsImporter)

  @Test
  internal fun `import one days reporting data`() {
    commands.importReports(date, date)

    verify(reportsImporter).import(date, date)
    verify(reportsImporter, times(1)).import(any(), any())
  }

  @Test
  internal fun `import two days reporting data`() {
    commands.importReports(date, date.plusDays(1))

    verify(reportsImporter).import(date, date)
    verify(reportsImporter).import(date.plusDays(1), date.plusDays(1))
    verify(reportsImporter, times(2)).import(any(), any())
  }
}