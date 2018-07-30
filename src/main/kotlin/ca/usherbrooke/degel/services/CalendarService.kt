package ca.usherbrooke.degel.services

import biweekly.Biweekly
import biweekly.ICalendar
import ca.usherbrooke.degel.clients.HorariusClient
import ca.usherbrooke.degel.entities.CalendarEntity
import ca.usherbrooke.degel.exceptions.CalendarCouldNotBeFetchedException
import ca.usherbrooke.degel.exceptions.CalendarKeyInvalidException
import ca.usherbrooke.degel.exceptions.CalendarKeyNotFoundException
import ca.usherbrooke.degel.exceptions.DegelException
import ca.usherbrooke.degel.models.Value
import ca.usherbrooke.degel.repositories.CalendarRepository
import feign.FeignException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface CalendarService {
    fun getCalendar(userId: UUID) : ICalendar
    fun getStoredCalendar(userId: UUID) : ICalendar?
    fun updateStoredCalendar(userId: UUID, calendar: ICalendar, fetchDate: Date)
    fun upsertCalendarKey(userId: UUID, key: String) : Value<String>
}

private val logger = KotlinLogging.logger {}

@Service
class CalendarServiceImpl(private val calendarRepository: CalendarRepository,
                          private val horariusClient: HorariusClient) : CalendarService {
    @Transactional
    @Throws(DegelException::class)
    override fun getCalendar(userId: UUID) : ICalendar {
        val calendarEntity = calendarRepository.findByUserId(userId)

        if (calendarEntity == null || calendarEntity.key.isBlank())
            throw CalendarKeyNotFoundException(userId)

        try {
            val rawCalendar = horariusClient.getCalendar(calendarEntity.key)
            return Biweekly.parse(rawCalendar).first()
        } catch (e: FeignException) {
            if(e.status() == HttpStatus.BAD_REQUEST.value()) {
                logger.error("Calendar key of user $userId is invalid")
                throw CalendarKeyInvalidException(userId)
            }

            throw CalendarCouldNotBeFetchedException(userId, e)
        }
    }

    @Transactional
    override fun getStoredCalendar(userId: UUID) : ICalendar? {
        val calendarEntity = calendarRepository.findByUserId(userId)
        return calendarEntity?.calendar
    }

    @Transactional
    override fun updateStoredCalendar(userId: UUID, calendar: ICalendar, fetchDate: Date) {
        calendarRepository.setCalendar(calendar, fetchDate, userId)
    }

    @Transactional
    override fun upsertCalendarKey(userId: UUID, key: String) : Value<String> {
        // Avoid some bad keys
        if(key.isBlank() || key.contains(" "))
            throw CalendarKeyInvalidException(userId)

        var calendarEntity = calendarRepository.findByUserId(userId)

        if(calendarEntity == null)
            calendarEntity = CalendarEntity(userId, key)
        else
            calendarEntity.key = key

        return Value(calendarRepository.save(calendarEntity).key)
    }
}