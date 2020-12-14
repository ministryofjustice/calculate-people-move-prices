package uk.gov.justice.digital.hmpps.pecs.jpc.importer.move

import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.report.Event
import uk.gov.justice.digital.hmpps.pecs.jpc.importer.report.EventType
import uk.gov.justice.digital.hmpps.pecs.jpc.move.Journey
import uk.gov.justice.digital.hmpps.pecs.jpc.move.JourneyState
import uk.gov.justice.digital.hmpps.pecs.jpc.move.Move
import uk.gov.justice.digital.hmpps.pecs.jpc.move.Move.Companion.CANCELLATION_REASON_CANCELLED_BY_PMU
import uk.gov.justice.digital.hmpps.pecs.jpc.move.MoveStatus
import uk.gov.justice.digital.hmpps.pecs.jpc.price.effectiveYearForDate
import java.time.LocalDate
import java.time.LocalDateTime

object MoveFilterer {

    private val logger = LoggerFactory.getLogger(javaClass)

    private fun MutableSet<Event>.hasEventType(eventType: EventType) = this.find { it.hasType(eventType) } != null

    private fun isCompleted(move: Move) = move.status == MoveStatus.completed

    /**
     * For a cancelled move to be billable it must be a previously accepted prison to prison move in a cancelled state.
     * It must have a cancellation reason of cancelled_by_pmu and have been cancelled after 3pm the day before the move date
     */
    fun isCancelledBillableMove(move: Move): Boolean {
        return move.status == MoveStatus.cancelled &&
            CANCELLATION_REASON_CANCELLED_BY_PMU == move.cancellationReason &&
            move.reportFromLocationType == "prison" &&
            move.toNomisAgencyId != null &&
            move.reportToLocationType == "prison" &&
            move.events.hasEventType(EventType.MOVE_ACCEPT) && // it was previously accepted
            move.events.hasEventType(EventType.MOVE_CANCEL) && // it was cancelled
            move.moveDate != null && move.events.find{
                it.hasType(EventType.MOVE_CANCEL)}?.occurredAt?.plusHours(9)?.isAfter(move.moveDate?.atStartOfDay()) ?: false
    }


    /**
     * A standard move is a completed move with a single completed journey that is billable, and no cancelled journeys
     * To be priced as a standard move, the journey as well as the move must be completed
     * There also should be no redirects after the move starts, but shouldn't need to check for this
     */
    fun isStandardMove(move: Move): Boolean {
        return isCompleted(move) &&
            with(move.journeys.map { it }) {
                count { it.stateIsAnyOf(JourneyState.completed) } == 1 &&
                count { it.stateIsAnyOf(JourneyState.completed) && it.billable } == 1 &&
                count { it.stateIsAnyOf(JourneyState.cancelled) } == 0
        }
    }

    /**
     * A simple lodging move must be a completed move with one journey lodging event OR 1 move lodging start and 1 move lodging end event
     * It must also have at 2 billable, completed journeys
     */
    fun isLongHaulMove(move: Move): Boolean {
        return isCompleted(move) &&
            (
                move.hasAllOf(EventType.JOURNEY_LODGING) ||
                move.hasAllOf(EventType.MOVE_LODGING_START, EventType.MOVE_LODGING_END)
            ) &&
            move.hasNoneOf(EventType.MOVE_REDIRECT, EventType.MOVE_LOCKOUT, EventType.JOURNEY_LOCKOUT) &&
            with(move.journeys.map { it }) {
                count { it.stateIsAnyOf(JourneyState.completed) && it.billable } == 2
            }
    }

    /**
     * A simple lockout move must be a completed move with one journey lockout event OR 1 move lockout event
     * And no redirect event. It must also have 2 or 3 completed, billable journeys
     */
    fun isLockoutMove(move: Move): Boolean {
        return isCompleted(move) &&
            move.hasAnyOf(EventType.MOVE_LOCKOUT, EventType.JOURNEY_LOCKOUT) &&
            move.hasNoneOf(EventType.MOVE_REDIRECT) &&
            with(move.journeys.map { it }) {
                count { it.stateIsAnyOf(JourneyState.completed) && it.billable } in 2..3
            }
    }

    /**
     * All other completed moves not covered by standard, redirect, long haul or lockout moves
     */
    fun isMultiTypeMove(move: Move) = isCompleted(move) &&
        !isStandardMove(move) &&
        !isRedirectionMove(move) &&
        !isLongHaulMove(move) &&
        !isLockoutMove(move)

/**Ω
     * A simple redirect is a completed move with 2 billable (cancelled or completed) journeys and
     * exactly one move redirect event that happened after the move started
     * If there is no move start event, it logs a warning and continues
     */
    fun isRedirectionMove(move: Move): Boolean {
        return isCompleted(move) &&
            move.hasAnyOf(EventType.MOVE_REDIRECT) &&
            move.hasNoneOf(EventType.JOURNEY_LODGING, EventType.MOVE_LODGING_START, EventType.MOVE_LODGING_END, EventType.MOVE_LOCKOUT, EventType.JOURNEY_LOCKOUT) &&
            when (val moveStartDate = move.events.find { it.type == EventType.MOVE_START.value }?.occurredAt) {
                null -> {
                    logger.warn("No move start date event found for move $move")
                    false
                }
                else -> {
                    move.events.count { it.hasType(EventType.MOVE_REDIRECT) && it.occurredAt.isAfter(moveStartDate) } == 1 &&
                    with(move.journeys.map { it }) {
                        count { it.stateIsAnyOf(JourneyState.completed, JourneyState.cancelled) && it.billable } == 2
                }
            }
        }
    }
}