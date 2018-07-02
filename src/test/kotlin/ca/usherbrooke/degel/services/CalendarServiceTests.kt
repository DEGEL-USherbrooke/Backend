package ca.usherbrooke.degel.services

import biweekly.Biweekly
import biweekly.ICalendar
import ca.usherbrooke.degel.clients.HorariusClient
import ca.usherbrooke.degel.entities.CalendarEntity
import ca.usherbrooke.degel.exceptions.CalendarCouldNotBeFetchedException
import ca.usherbrooke.degel.exceptions.CalendarKeyInvalidException
import ca.usherbrooke.degel.exceptions.CalendarKeyNotFoundException
import ca.usherbrooke.degel.repositories.CalendarRepository
import feign.FeignException
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.apache.commons.io.IOUtils
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.nio.charset.StandardCharsets
import java.util.*

private class BadRequestException : FeignException(400, "You dumb or what")

private class FeignRandomException : FeignException("What is going on here")

@RunWith(MockitoJUnitRunner::class)
class CalendarServiceTests {
    companion object {
        const val CALENDAR_KEY = "may the force be with you"
        const val ANOTHER_CALENDAR_KEY = "luke you are my son"
        val USER_ID = UUID.randomUUID()
        val CALENDAR_DATA = IOUtils.toString(this::class.java.getResourceAsStream("/calendars/icalendar.ics"), StandardCharsets.UTF_8)
        val CALENDAR = Biweekly.parse(CALENDAR_DATA).first()
        val NOW = Date()
    }

    val calendarRepositoryMock = mockk<CalendarRepository>()

    val horariusClientMock = mockk<HorariusClient>()

    val calendarService = CalendarServiceImpl(calendarRepositoryMock, horariusClientMock)

    @Test
    fun `if no calendar insert it`() {
        val entity = slot<CalendarEntity>()

        every { calendarRepositoryMock.findByUserId(any()) } returns null
        every { calendarRepositoryMock.save(capture(entity)) } answers { entity.captured }

        calendarService.upsertCalendarKey(USER_ID, CALENDAR_KEY)

        verify { calendarRepositoryMock.save(any<CalendarEntity>()) }
        assertNull(entity.captured.id)
        assertEquals(CALENDAR_KEY, entity.captured.key)
    }

    @Test
    fun `if calendar present update the key`() {
        val entity = slot<CalendarEntity>()

        every { calendarRepositoryMock.findByUserId(any()) } returns CalendarEntity(USER_ID, CALENDAR_KEY)
                .also { s -> s.id = UUID.randomUUID() }
        every { calendarRepositoryMock.save(capture(entity)) } answers { entity.captured }

        calendarService.upsertCalendarKey(USER_ID, ANOTHER_CALENDAR_KEY)

        verify { calendarRepositoryMock.save(any<CalendarEntity>()) }
        assertNotNull(entity.captured.id)
        assertEquals(ANOTHER_CALENDAR_KEY, entity.captured.key)
    }

    @Test(expected = CalendarKeyNotFoundException::class)
    fun `if no calendar throw`() {
        every { calendarRepositoryMock.findByUserId(any()) } returns null

        calendarService.getCalendar(USER_ID)
    }

    @Test(expected = CalendarKeyNotFoundException::class)
    fun `if empty calendar key throw`() {
        every { calendarRepositoryMock.findByUserId(any()) } returns CalendarEntity(USER_ID, "")

        calendarService.getCalendar(USER_ID)
    }

    @Test
    fun `get user calendar`() {
        every { calendarRepositoryMock.findByUserId(USER_ID) } returns CalendarEntity(USER_ID, CALENDAR_KEY)
                .also { s -> s.id = UUID.randomUUID() }
        every { horariusClientMock.getCalendar(CALENDAR_KEY) } returns CALENDAR_DATA

        val data = calendarService.getCalendar(USER_ID)

        assertNotNull(data)
    }

    @Test(expected = CalendarKeyInvalidException::class)
    fun `if invalid key throw`() {
        every { calendarRepositoryMock.findByUserId(USER_ID) } returns CalendarEntity(USER_ID, CALENDAR_KEY)
                .also { s -> s.id = UUID.randomUUID() }
        every { horariusClientMock.getCalendar(CALENDAR_KEY) } throws BadRequestException()

        calendarService.getCalendar(USER_ID)
    }

    @Test(expected = CalendarCouldNotBeFetchedException::class)
    fun `if fetching error throw`() {
        every { calendarRepositoryMock.findByUserId(USER_ID) } returns CalendarEntity(USER_ID, CALENDAR_KEY)
                .also { s -> s.id = UUID.randomUUID() }
        every { horariusClientMock.getCalendar(CALENDAR_KEY) } throws FeignRandomException()

        calendarService.getCalendar(USER_ID)
    }

    @Test
    fun `get stored calendar`() {
        every { calendarRepositoryMock.findByUserId(USER_ID) } returns CalendarEntity(USER_ID, CALENDAR_KEY, NOW, CALENDAR)
                .also { s -> s.id = UUID.randomUUID() }

        val calendar = calendarService.getStoredCalendar(USER_ID)

        assertEquals(CALENDAR, calendar)
    }

    @Test
    fun `update calendar for user`() {
        val calendar = slot<ICalendar>()
        val userId = slot<UUID>()

        every { calendarRepositoryMock.setCalendar(capture(calendar), any(), capture(userId)) } returns Unit

        calendarService.updateStoredCalendar(USER_ID, CALENDAR, NOW)

        verify { calendarRepositoryMock.setCalendar(any(), any(), any()) }
        assertEquals(CALENDAR, calendar.captured)
        assertEquals(USER_ID, userId.captured)
    }
}