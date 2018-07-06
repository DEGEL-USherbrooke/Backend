package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.NotifyUsClient
import ca.usherbrooke.degel.exceptions.FailedRegisterNotificationTypeException
import ca.usherbrooke.degel.exceptions.FailedSendNotificationException
import ca.usherbrooke.degel.models.notification.notifyus.NUNotification
import ca.usherbrooke.degel.models.notification.notifyus.NUNotificationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface IntegrationService {
    fun registerNotifyUsNotificationType(notificationType: NUNotificationType): NUNotificationType
    fun sendNotifyUsNotification(nuNotification: NUNotification): NUNotification
}

@Service
class IntegrationServiceImpl(val notifyUsClient: NotifyUsClient,
                             @Value("\${app.notification.notifyus.token}") val notifyUsToken: String) : IntegrationService {
    @Throws(FailedRegisterNotificationTypeException::class)
    override fun registerNotifyUsNotificationType(nuNotificationType: NUNotificationType): NUNotificationType {
        try {
            return notifyUsClient.registerNotificationType(notifyUsToken, nuNotificationType)
        } catch (e: Exception) {
            throw FailedRegisterNotificationTypeException(nuNotificationType.notificationTypeId)
        }
    }

    @Throws(FailedSendNotificationException::class)
    override fun sendNotifyUsNotification(nuNotification: NUNotification): NUNotification {
        try {
            return notifyUsClient.sendNotification(notifyUsToken, nuNotification)
        } catch (e: Exception) {
            throw FailedSendNotificationException(nuNotification.cip)
        }
    }
}