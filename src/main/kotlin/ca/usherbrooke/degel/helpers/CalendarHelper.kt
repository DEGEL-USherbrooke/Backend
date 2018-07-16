package ca.usherbrooke.degel.helpers

import biweekly.ICalendar
import biweekly.component.VEvent
import ca.usherbrooke.degel.helpers.criterias.NotificationCriteria
import ca.usherbrooke.degel.models.CalendarDiff
import ca.usherbrooke.degel.models.notification.Notification
import org.springframework.context.MessageSource
import java.util.*

object CalendarHelper {
    private val notificationCriteria = NotificationCriteria()

    fun diff(calendar: ICalendar, storedCalendar: ICalendar) : CalendarDiff {
        // Copy old events
        val storedEvents = storedCalendar.events.toMutableList()

        val addedEvents = mutableListOf<VEvent>()
        val removedEvents = mutableListOf<VEvent>()
        val modifiedEvents  = mutableListOf<VEvent>()

        var foundMatch = false
        for(event in calendar.events) {
            // Search for the same UID in the stored events
            for(i in storedEvents.indices) {
                // Found a match
                if (event.uid == storedEvents[i].uid) {
                    if(!isSameEvent(event, storedEvents[i]))
                        modifiedEvents.add(event)

                    foundMatch = true
                    storedEvents.removeAt(i)
                    break
                }
            }

            // If no match, it's a new event
            if(!foundMatch)
                addedEvents.add(event)

            foundMatch = false
        }

        // Remaining events are removed
        removedEvents.addAll(storedEvents)

        return CalendarDiff(addedEvents, removedEvents, modifiedEvents)
    }

    fun isSameEvent(event: VEvent, eventStored: VEvent) : Boolean {
        if (event.uid != eventStored.uid)
            return false

        if (event.dateStart != eventStored.dateStart)
            return false

        if (event.dateEnd != eventStored.dateEnd)
            return false

        if (event.location != eventStored.location)
            return false

        return true
    }

    fun diffToNotifications(userId: UUID, calendarDiff: CalendarDiff, messageSource: MessageSource): List<Notification> {
        val notifications = mutableListOf<Notification>()

        // New events
        val addedEvents = notificationCriteria.apply(calendarDiff.addedEvents)
        for (addedEvent: VEvent in addedEvents) {
            notifications.add(Notification(
                    null,
                    userId,
                    messageSource.getMessage("calendar.event.added.title", null, Locale.FRENCH),
                    messageSource.getMessage("calendar.event.added.description",
                            arrayOf(addedEvent.summary?.value ?: "",
                                    addedEvent.location?.value ?: "",
                                    addedEvent.dateStart?.value ?: ""),
                            Locale.FRENCH)))
        }

        // Removed events
        val removedEvents = notificationCriteria.apply(calendarDiff.removedEvents)
        for (removedEvent: VEvent in removedEvents) {
            notifications.add(Notification(
                    null,
                    userId,
                    messageSource.getMessage("calendar.event.removed.title", null, Locale.FRENCH),
                    messageSource.getMessage("calendar.event.removed.description",
                            arrayOf(removedEvent.summary?.value ?: "",
                                    removedEvent.location?.value ?: "",
                                    removedEvent.dateStart?.value ?: ""),
                            Locale.FRENCH)))
        }

        // Changed events
        val modifiedEvents = notificationCriteria.apply(calendarDiff.modifiedEvents)
        for (modifiedEvent: VEvent in modifiedEvents) {
            notifications.add(Notification(
                    null,
                    userId,
                    messageSource.getMessage("calendar.event.modified.title", null, Locale.FRENCH),
                    messageSource.getMessage("calendar.event.modified.description",
                            arrayOf(modifiedEvent.summary?.value ?: "",
                                    modifiedEvent.location?.value ?: "",
                                    modifiedEvent.dateStart?.value ?: ""),
                            Locale.FRENCH)))
        }

        return notifications
    }
}