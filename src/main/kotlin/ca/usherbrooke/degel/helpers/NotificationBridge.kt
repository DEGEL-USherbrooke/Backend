package ca.usherbrooke.degel.helpers

import ca.usherbrooke.degel.models.notification.Notification
import ca.usherbrooke.degel.models.notification.notifyus.NUNotification
import ca.usherbrooke.degel.services.IntegrationService
import ca.usherbrooke.degel.services.NotificationService
import ca.usherbrooke.degel.services.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotificationBridge(private val notificationService: NotificationService,
                         private val integrationService: IntegrationService,
                         private val userService: UserService,
                         @Value("\${app.integration.notifyus}")
                         private val notifyUsEnabled: Boolean,
                         @Value("\${app.notification.notifyus.calendar-notification}")
                         private val notifyUsNotificationType: String) {
    fun sendCalendarNotification(notification: Notification) {
        if(notifyUsEnabled) {
            val cip = userService.getUser(notification.userId!!).cip

            val nuNotification = NUNotification(
                    cip,
                    notifyUsNotificationType,
                    notification.title,
                    notification.description
            )

            integrationService.sendNotifyUsNotification(nuNotification)
        } else {
            notificationService.sendNotification(notification)
        }
    }
}