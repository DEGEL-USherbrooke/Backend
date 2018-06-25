package ca.usherbrooke.degel.services

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import ca.usherbrooke.degel.clients.HorariusClient
import ca.usherbrooke.degel.entities.CalendarEntity
import ca.usherbrooke.degel.exceptions.CalendarCouldNotBeFetchedException
import ca.usherbrooke.degel.exceptions.CalendarKeyNotFoundException
import ca.usherbrooke.degel.exceptions.DegelException
import ca.usherbrooke.degel.models.CalendarDiff
import ca.usherbrooke.degel.models.Value
import ca.usherbrooke.degel.repositories.CalendarRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface CalendarService {
    fun getCalendar(userId: UUID) : String
    fun upsertCalendarKey(userId: UUID, key: String) : Value<String>
}

private val logger = KotlinLogging.logger {}

@Service
class CalendarServiceImpl(val calendarRepository: CalendarRepository,
                          val horariusClient: HorariusClient) : CalendarService {
    @Transactional
    @Throws(DegelException::class)
    override fun getCalendar(userId: UUID) : String {
        val calendarEntity = calendarRepository.findByUserId(userId)

        if (calendarEntity == null || calendarEntity.key.isEmpty())
            throw CalendarKeyNotFoundException(userId)

        try {
            val rawCalendar = horariusClient.getCalendar(calendarEntity.key)
            calendarEntity.calendar = Biweekly.parse(rawCalendar).first()
            calendarEntity.lastFetch = Date()

            calendarRepository.save(calendarEntity)

            return Biweekly.writeJson(calendarEntity.calendar).go()
        } catch (e: Exception) {
            logger.error("Error when fetching calendar", e)
            throw CalendarCouldNotBeFetchedException(userId, e)
        }
    }

    @Transactional
    override fun upsertCalendarKey(userId: UUID, key: String) : Value<String> {
        var calendarEntity = calendarRepository.findByUserId(userId)

        if(calendarEntity == null)
            calendarEntity = CalendarEntity(userId, key)
        else
            calendarEntity.key = key

        return Value(calendarRepository.save(calendarEntity).key)
    }

    fun diffCalendars(calendar: ICalendar, storedCalendar: ICalendar) : CalendarDiff {
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
                    if(!sameEvent(event, storedEvents[i]))
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

    fun sameEvent(event: VEvent, eventStored: VEvent) : Boolean {
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