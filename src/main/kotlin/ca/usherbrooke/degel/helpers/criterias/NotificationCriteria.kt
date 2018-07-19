package ca.usherbrooke.degel.helpers.criterias

import biweekly.component.VEvent
import ca.usherbrooke.degel.helpers.extensions.toDate
import java.time.LocalDate
import java.util.*

class NotificationCriteria : Criteria<VEvent> {
    override fun apply(input: List<VEvent>): List<VEvent> {
        return NoPastEvent() and NoEventsMonthsInAdvance(3) apply(input)
    }
}

class NoPastEvent : Criteria<VEvent> {
    override fun apply(input: List<VEvent>): List<VEvent> {
        val today = Date()
        return input.filter { it.dateEnd.value > today }
    }
}

class NoEventsMonthsInAdvance(private val numberOfMonths : Long) : Criteria<VEvent> {
    override fun apply(input: List<VEvent>): List<VEvent> {
        val futureDate = LocalDate.now().plusMonths( numberOfMonths).toDate()
        return input.filter { it.dateEnd.value < futureDate }
    }
}