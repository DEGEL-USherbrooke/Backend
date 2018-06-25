package ca.usherbrooke.degel.services

import biweekly.Biweekly
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
    fun getCalendar(userId: UUID) : String
    fun upsertCalendarKey(userId: UUID, key: String) : Value<String>
}

private val logger = KotlinLogging.logger {}

@Service
class CalendarServiceImpl(private val calendarRepository: CalendarRepository,
                          private val horariusClient: HorariusClient) : CalendarService {
    @Transactional
    @Throws(DegelException::class)
    override fun getCalendar(userId: UUID) : String {
        logger.info("User $userId requests his calendar")

        val calendarEntity = calendarRepository.findByUserId(userId)

        if (calendarEntity == null || calendarEntity.key.isEmpty())
            throw CalendarKeyNotFoundException(userId)

        try {
            val rawCalendar = horariusClient.getCalendar(calendarEntity.key)
            calendarEntity.calendar = Biweekly.parse(rawCalendar).first()
            calendarEntity.lastFetch = Date()

            calendarRepository.save(calendarEntity)

            return Biweekly.writeJson(calendarEntity.calendar).go()
        } catch (e: FeignException) {
            if(e.status() == HttpStatus.BAD_REQUEST.value())
                throw CalendarKeyInvalidException(userId)

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
}