package ca.usherbrooke.degel.helpers

import biweekly.Biweekly
import org.apache.commons.io.IOUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.nio.charset.StandardCharsets


@RunWith(MockitoJUnitRunner::class)
class CalendarHelperTests {

    @Test
    fun `test diff calendars`() {
        val storedCalendarString = IOUtils.toString(this.javaClass.getResourceAsStream("/calendars/icalendar.ics"), StandardCharsets.UTF_8)
        val storedCalendar = Biweekly.parse(storedCalendarString).first()

        val calendarString = IOUtils.toString(this.javaClass.getResourceAsStream("/calendars/icalendar_2.ics"), StandardCharsets.UTF_8)
        val calendar = Biweekly.parse(calendarString).first()

        val calendarDiff = CalendarHelper.diff(calendar, storedCalendar)

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

        assertFalse(CalendarHelper.isSameEvent(calendar.events[0], calendar.events[1]))
        assertFalse(CalendarHelper.isSameEvent(calendar.events[0], calendar.events[2]))
        assertFalse(CalendarHelper.isSameEvent(calendar.events[0], calendar.events[3]))
        assertFalse(CalendarHelper.isSameEvent(calendar.events[0], calendar.events[4]))
    }

}