package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.ExpoClient
import ca.usherbrooke.degel.entities.NotificationTokenEntity
import ca.usherbrooke.degel.exceptions.FailedSendNUNotificationException
import ca.usherbrooke.degel.models.notification.ExpoNotification
import ca.usherbrooke.degel.models.notification.NotificationToken
import ca.usherbrooke.degel.models.notification.Notification
import ca.usherbrooke.degel.repositories.NotificationTokenRepository
import ca.usherbrooke.degel.repositories.UserRepository
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificationService {
    fun notificationRegister(id: UUID, tokenExpo: NotificationToken): NotificationToken
    fun sendNotification(notification: Notification)
    fun sendNotificationAll(notification: Notification)
}

@Service
class NotificationServiceImpl(private val notificationTokenRepository: NotificationTokenRepository,
                              private val userRepository: UserRepository,
                              private val settingsService: SettingsService,
                              private val expoClient: ExpoClient) : NotificationService {
    companion object: KLogging()

    @Transactional
    override fun sendNotification(notification: Notification) {
        // Find the corresponding user
        var userId = notification.userId

        if (userId == null && notification.cip != null) {
            val user = userRepository.findByCip(notification.cip)
            userId = user?.id
        }

        // If cannot find it, do nothing (we don't know the user)
        if (userId == null) {
            logger.error("Cannot send notification. Unknown user: ${notification.cip}.")
            return
        }

        // Check if user doesn't want notifications
        val settings = settingsService.getSettings(userId)
        if (!settings.notifications.mobile)
            return

        // Send notification to all devices
        val notificationTokens = notificationTokenRepository.findByUserId(userId)

        notificationTokens?.let {
            sendNotificationToTokens(notification, it)
        }
    }

    override fun sendNotificationAll(notification: Notification) {
        logger.info { "Sending notification to all users" }

        val notificationTokens = notificationTokenRepository.findAll()
        sendNotificationToTokens(notification, notificationTokens)
    }

    override fun notificationRegister(id: UUID, tokenExpo: NotificationToken): NotificationToken {
        val oldTokenExpo = notificationTokenRepository.findByExpoToken(tokenExpo.expoToken)

        if (oldTokenExpo != null) {
            deleteOldNotificationEntity(oldTokenExpo)
        }

        val notificationEntity = NotificationTokenEntity.fromModel(id, NotificationToken(tokenExpo.expoToken))

        return addNewNotificationEntity(notificationEntity)
    }

    @Transactional
    fun deleteOldNotificationEntity(notificationTokenEntity: NotificationTokenEntity) {
        notificationTokenRepository.delete(notificationTokenEntity)
    }

    @Transactional
    fun addNewNotificationEntity(notificationTokenEntity: NotificationTokenEntity): NotificationToken {
        return notificationTokenRepository.save(notificationTokenEntity).toModel()
    }

    private fun sendNotificationToTokens(notification: Notification, notificationTokens: List<NotificationTokenEntity>) {
        for (notificationToken in notificationTokens) {
            val expoNotification = ExpoNotification(notificationToken.expoToken, notification.title, notification.description)

            try {
                expoClient.sendNotification(expoNotification)
            } catch (e: Exception) {
                logger.error { "Failed to send notification to devive ${expoNotification.to}" }
            }
        }
    }
}
