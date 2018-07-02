package ca.usherbrooke.degel.helpers

import biweekly.ICalendar
import biweekly.component.VEvent
import ca.usherbrooke.degel.models.CalendarDiff

object CalendarHelper {
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
}