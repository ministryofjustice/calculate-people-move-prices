package uk.gov.justice.digital.hmpps.pecs.jpc.integration.pages

import uk.gov.justice.digital.hmpps.pecs.jpc.importer.report.Person
import uk.gov.justice.digital.hmpps.pecs.jpc.move.Move
import uk.gov.justice.digital.hmpps.pecs.jpc.move.MoveStatus
import uk.gov.justice.digital.hmpps.pecs.jpc.move.MoveType
import uk.gov.justice.digital.hmpps.pecs.jpc.price.Supplier
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * The integration test data comes from the DEV Spring Profile data loaded via the database migration scripts at startup.
 */

private val billyTheKid =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER1",
    firstNames = "Billy the",
    lastName = "Kid",
    dateOfBirth = LocalDate.of(1980, 12, 25),
    gender = "male"
  )

private val bonnieElizabeth =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER3",
    firstNames = "Bonnie Elizabeth",
    lastName = "Parker",
    dateOfBirth = LocalDate.of(1910, 10, 1),
    gender = "female"
  )

private val ronnieBiggs =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER4",
    firstNames = "Ronnie",
    lastName = "Biggs",
    dateOfBirth = LocalDate.of(1929, 8, 8),
    gender = "male"
  )

private val fredBloggs =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER5",
    firstNames = "Fred",
    lastName = "Bloggs",
    dateOfBirth = LocalDate.of(1950, 4, 12),
    gender = "male"
  )

private val janeBloggs =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER6",
    firstNames = "Jane",
    lastName = "Bloggs",
    dateOfBirth = LocalDate.of(1961, 10, 22),
    gender = "female"
  )

private val donaldDuck =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER7",
    firstNames = "Donald",
    lastName = "Duck",
    dateOfBirth = LocalDate.of(1940, 9, 27),
    gender = "male"
  )

private val professorMoriarty =
  Person(
    personId = "_",
    updatedAt = LocalDateTime.now(),
    prisonNumber = "PRISONER8",
    firstNames = "Professor",
    lastName = "Moriarty",
    dateOfBirth = LocalDate.of(1880, 7, 10),
    gender = "male"
  )

object Dec2020MoveData {

  fun standardMoveM4() =
    Move(
      moveId = "M4",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.STANDARD,
      status = MoveStatus.completed,
      reference = "STANDARDMOVE2",
      fromNomisAgencyId = "PRISON3",
      fromSiteName = "PRISON THREE",
      toNomisAgencyId = "PRISON4",
      toSiteName = "PRISON FOUR",
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = LocalDateTime.of(2020, 12, 1, 10, 20),
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 1, 12, 20),
      person = Person(
        personId = "_",
        updatedAt = LocalDateTime.now(),
        prisonNumber = "PRISONER2",
        firstNames = "Clyde Chestnut",
        lastName = "Barrow",
        dateOfBirth = LocalDate.of(1909, 3, 24),
        gender = "male"
      )
    )

  fun longHaulMoveM30() =
    Move(
      moveId = "M30",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.LONG_HAUL,
      status = MoveStatus.completed,
      reference = "LONGMOVE",
      fromNomisAgencyId = "PRISON1",
      fromSiteName = "PRISON ONE",
      toNomisAgencyId = "BOG",
      toSiteName = "BOG", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = LocalDateTime.of(2020, 12, 3, 12, 0),
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 4, 11, 20),
      person = billyTheKid
    )

  fun redirectMoveM20() =
    Move(
      moveId = "M20",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.REDIRECTION,
      status = MoveStatus.completed,
      reference = "REDIRMOVE",
      fromNomisAgencyId = "PRISON1",
      fromSiteName = "PRISON ONE",
      toNomisAgencyId = "GNI",
      toSiteName = "GNI", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = LocalDateTime.of(2020, 12, 2, 10, 20),
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 2, 14, 20),
      person = bonnieElizabeth
    )

  fun lockoutMoveM40() =
    Move(
      moveId = "M40",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.LOCKOUT,
      status = MoveStatus.completed,
      reference = "LOCKMOVE",
      fromNomisAgencyId = "COURT1",
      fromSiteName = "COURT ONE",
      toNomisAgencyId = "PRISON1",
      toSiteName = "PRISON ONE",
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = LocalDateTime.of(2020, 12, 4, 10, 20),
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 5, 10, 20),
      person = bonnieElizabeth
    )

  fun multiMoveM50() =
    Move(
      moveId = "M50",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.MULTI,
      status = MoveStatus.completed,
      reference = "MULTIMOVE",
      fromNomisAgencyId = "PRISON1",
      fromSiteName = "PRISON ONE",
      toNomisAgencyId = "PRISON3",
      toSiteName = "PRISON THREE",
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = LocalDateTime.of(2020, 12, 5, 10, 20),
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 6, 10, 20),
      person = billyTheKid
    )

  fun cancelledMoveM60() =
    Move(
      moveId = "M60",
      updatedAt = LocalDateTime.now(),
      supplier = Supplier.GEOAMEY,
      moveType = MoveType.CANCELLED,
      status = MoveStatus.completed,
      reference = "CANCELLED_BEFORE_3PM",
      fromNomisAgencyId = "PRISON1",
      fromSiteName = "PRISON ONE",
      toNomisAgencyId = "GNI",
      toSiteName = "GNI", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = null,
      dropOffOrCancelledDateTime = LocalDateTime.of(2020, 12, 6, 14, 59),
      person = billyTheKid
    )
}

object PresentDayMoveData {

  private val today = LocalDate.now()

  private val startHoursOffset = 10L

  private val endHoursOffset = 12L

  fun standardMoveSM1() =
    Move(
      moveId = "SM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.STANDARD,
      status = MoveStatus.completed,
      reference = "STANDARDSM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset),
      person = billyTheKid
    )

  fun redirectMoveRM1() =
    Move(
      moveId = "RM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.REDIRECTION,
      status = MoveStatus.completed,
      reference = "REDIRECTIONRM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset),
      person = ronnieBiggs
    )

  fun longHaulMoveLHM1() =
    Move(
      moveId = "LHM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.LONG_HAUL,
      status = MoveStatus.completed,
      reference = "LONG_HAULLHM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset).plusDays(1),
      person = fredBloggs
    )

  fun lockoutMoveLM1() =
    Move(
      moveId = "LM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.LOCKOUT,
      status = MoveStatus.completed,
      reference = "LOCKOUTLM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset).plusDays(1),
      person = janeBloggs
    )

  fun multiMoveMM1() =
    Move(
      moveId = "MM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.MULTI,
      status = MoveStatus.completed,
      reference = "MULTIMM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset).plusDays(1),
      person = donaldDuck
    )

  fun cancelledMoveCM1() =
    Move(
      moveId = "CM1",
      updatedAt = today.atStartOfDay(),
      supplier = Supplier.SERCO,
      moveType = MoveType.CANCELLED,
      status = MoveStatus.completed,
      reference = "CANCELLEDCM1",
      fromNomisAgencyId = "FROM_AGENCY",
      fromSiteName = "FROM_AGENCY", // Test data is not mapped so will default to the agency ID
      toNomisAgencyId = "TO_AGENCY",
      toSiteName = "TO_AGENCY", // Test data is not mapped so will default to the agency ID
      reportFromLocationType = "prison",
      reportToLocationType = "prison",
      pickUpDateTime = today.atStartOfDay().plusHours(startHoursOffset),
      dropOffOrCancelledDateTime = today.atStartOfDay().plusHours(endHoursOffset),
      person = professorMoriarty
    )
}
