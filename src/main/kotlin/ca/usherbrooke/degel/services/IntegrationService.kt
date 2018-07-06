package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.NotifyUsClient
import ca.usherbrooke.degel.exceptions.FailedRegisterNotificationTypeException
import ca.usherbrooke.degel.exceptions.FailedSendNotificationException
import ca.usherbrooke.degel.models.NotifyUs.Notification
import ca.usherbrooke.degel.models.NotifyUs.NotificationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface IntegrationService {
    fun registerNotifyUsNotificationType(notificationType: NotificationType): NotificationType
    fun sendNotifyUsNotification(notification: Notification): Notification
}

@Service
class IntegrationServiceImpl(val notifyUsClient: NotifyUsClient,
                             @Value("\${app.notification.notifyus.token}") val notifyUsToken: String) : IntegrationService {
    @Throws(FailedRegisterNotificationTypeException::class)
    override fun registerNotifyUsNotificationType(notificationType: NotificationType): NotificationType {
        try {
            return notifyUsClient.registerNotificationType(notifyUsToken, notificationType)
        } catch (e: Exception) {
            throw FailedRegisterNotificationTypeException(notificationType.notificationTypeId)
        }
    }

    @Throws(FailedSendNotificationException::class)
    override fun sendNotifyUsNotification(notification: Notification): Notification {
        try {
            return notifyUsClient.sendNotification(notifyUsToken, notification)
        } catch (e: Exception) {
            throw FailedSendNotificationException(notification.cip)
        }
    }
}