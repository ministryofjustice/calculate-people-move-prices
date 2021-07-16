package uk.gov.justice.digital.hmpps.pecs.jpc.integration

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.cancelledMoveM60
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.lockoutMoveM40
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.longHaulMoveM30
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.multiMoveM50
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.redirectMoveM20
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Dec2020MoveData.standardMoveM4
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.ChooseSupplier
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.Dashboard
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.Login
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.MoveDetails
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.MovesByType
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.Pages.SelectMonthYear
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.cancelledMoveCM1
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.lockoutMoveLM1
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.longHaulMoveLHM1
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.multiMoveMM1
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.redirectMoveRM1
import uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages.PresentDayMoveData.standardMoveSM1
import uk.gov.justice.digital.hmpps.pecs.jpc.move.Move
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Supplier
import java.time.LocalDate
import java.time.Month
import java.time.Year

internal class ViewAllMoveTypesTest : IntegrationTest() {

  @Test
  fun `view one of each move type for Serco today`() {
    goToPage(Dashboard)

    isAtPage(Login).login()

    isAtPage(ChooseSupplier).choose(Supplier.SERCO)

    isAtPage(Dashboard)
      .isAtMonthYear(LocalDate.now().month, Year.now())
      .navigateToSelectMonthPage()

    listOf(
      standardMoveSM1(),
      redirectMoveRM1(),
      longHaulMoveLHM1(),
      lockoutMoveLM1(),
      multiMoveMM1(),
      cancelledMoveCM1()
    ).forEach { move -> verifyDetailsOf(move, LocalDate.now().month, Year.now()) }
  }

  @Test
  fun `view one of each move type for GEOAmey in a previous month (Dec 2020)`() {
    goToPage(Dashboard)

    isAtPage(Login).login()

    isAtPage(ChooseSupplier).choose(Supplier.GEOAMEY)

    isAtPage(Dashboard)
      .isAtMonthYear(LocalDate.now().month, Year.now())
      .navigateToSelectMonthPage()

    isAtPage(SelectMonthYear).navigateToDashboardFor("dec 2020")

    listOf(
      standardMoveM4(),
      longHaulMoveM30(),
      redirectMoveM20(),
      lockoutMoveM40(),
      multiMoveM50(),
      cancelledMoveM60()
    ).forEach { move -> verifyDetailsOf(move, Month.DECEMBER, Year.of(2020)) }
  }

  private fun verifyDetailsOf(move: Move, month: Month, year: Year) {
    goToPage(Dashboard)

    isAtPage(Dashboard)
      .isAtMonthYear(month, year)
      .navigateToMovesBy(move.moveType!!)

    isAtPage(MovesByType)
      .isAtPageFor(move.moveType!!)
      .navigateToDetailsFor(move)

    isAtPage(MoveDetails).isAtPageFor(move)
  }
}
