package ca.usherbrooke.degel.tasks

import ca.usherbrooke.degel.helpers.CalendarHelper
import ca.usherbrooke.degel.helpers.NotificationBridge
import ca.usherbrooke.degel.services.CalendarService
import ca.usherbrooke.degel.services.UserService
import mu.KotlinLogging
import org.springframework.context.MessageSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class CalendarTask(private val userService: UserService,
                   private val calendarService: CalendarService,
                   private val notificationBridge: NotificationBridge,
                   private val messageSource: MessageSource) {
    @Scheduled(fixedDelayString = "\${app.calendar.refresh-delay}")
    fun updateCalendars() {
        logger.info("Updating calendars for all users")

        val userIds = userService.getUsersId()

        for (userId in userIds) {
            try {
                val calendar = calendarService.getCalendar(userId)
                val storedCalendar= calendarService.getStoredCalendar(userId)

                if(storedCalendar == null) {
                    calendarService.updateStoredCalendar(userId, calendar, Date())
                    continue
                }

                val calendarDiff = CalendarHelper.diff(calendar, storedCalendar)
                val notifications = CalendarHelper.diffToNotifications(userId, calendarDiff, messageSource)

                for (notification in notifications) {
                    notificationBridge.sendCalendarNotification(notification)
                }

                calendarService.updateStoredCalendar(userId, calendar, Date())
            } catch (e: Exception) {
                logger.warn("Unable to update calendar for user $userId")
            }
        }
    }
}