package ca.usherbrooke.degel.services

import org.apache.commons.io.IOUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.nio.charset.StandardCharsets
import biweekly.Biweekly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse


@RunWith(MockitoJUnitRunner::class)
class CalendarServiceTests {

    val calendarService = CalendarServiceImpl()

    @Test
    fun `test diff calendars`() {
        val storedCalendarString = IOUtils.toString(this.javaClass.getResourceAsStream("/calendars/icalendar.ics"), StandardCharsets.UTF_8)
        val storedCalendar = Biweekly.parse(storedCalendarString).first()

        val calendarString = IOUtils.toString(this.javaClass.getResourceAsStream("/calendars/icalendar_2.ics"), StandardCharsets.UTF_8)
        val calendar = Biweekly.parse(calendarString).first()

        val calendarDiff = calendarService.diffCalendars(calendar, storedCalendar)

        assertEquals(1, calendarDiff.addedEvents.size)
        assertEquals("50906", calendarDiff.addedEvents[0].uid.value)
        assertEquals(1, calendarDiff.removedEvents.size)
        assertEquals("61631", calendarDiff.removedEvents[0].uid.value)
        assertEquals(1, calendarDiff.modifiedEvents.size)
        assertEquals("61739", calendarDiff.modifiedEvents[0].uid.value)
    }

    @Test
    fun `test same events`() {
        val calendarString = IOUtils.toString(this.javaClass.getResourceAsStream("/calendars/icalendar_3.ics"), StandardCharsets.UTF_8)
        val calendar = Biweekly.parse(calendarString).first()

        assertFalse(calendarService.sameEvent(calendar.events[0], calendar.events[1]))
        assertFalse(calendarService.sameEvent(calendar.events[0], calendar.events[2]))
        assertFalse(calendarService.sameEvent(calendar.events[0], calendar.events[3]))
        assertFalse(calendarService.sameEvent(calendar.events[0], calendar.events[4]))
    }

}