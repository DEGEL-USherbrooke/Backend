package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.ExpoNotificationClient
import ca.usherbrooke.degel.entities.NotificationEntity
import ca.usherbrooke.degel.models.ExpoNotification
import ca.usherbrooke.degel.models.Notification
import ca.usherbrooke.degel.models.NotificationContent
import ca.usherbrooke.degel.repositories.NotificationRepository
import ca.usherbrooke.degel.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificationService {
    fun notificationRegister(id: UUID, tokenExpo: Notification): Notification
    fun sendNotification(notificationContent: NotificationContent)
}

@Service
class NotificationServiceImpl(private val notificationRepository: NotificationRepository,
                              private val userRepository: UserRepository,
                              private val expoNotificationClient: ExpoNotificationClient) : NotificationService {
    @Transactional
    override fun sendNotification(notificationContent: NotificationContent) {
        val user = userRepository.findByCip(notificationContent.cip)

        if (user != null) {
            val notificationTokens = notificationRepository.findByUserId(user.id!!)

            if (notificationTokens != null) {
                for (notificationToken: NotificationEntity in notificationTokens) {
                    val expoNotification =  ExpoNotification(notificationToken.expoToken, notificationContent.title, notificationContent.description)
                    expoNotificationClient.sendNotification(expoNotification)
                }
            }
        }
    }

    override fun notificationRegister(id: UUID, tokenExpo: Notification): Notification {
        val oldTokenExpo = notificationRepository.findByExpoToken(tokenExpo.expoToken)

        if (oldTokenExpo != null) {
            deleteOldNotificationEntity(oldTokenExpo)
        }

        val notificationEntity = NotificationEntity.fromModel(id, Notification(tokenExpo.expoToken))

        return addNewNotificationEntity(notificationEntity)
    }

    @Transactional
    fun deleteOldNotificationEntity(notificationEntity: NotificationEntity) {
        notificationRepository.delete(notificationEntity)
    }

    @Transactional
    fun addNewNotificationEntity(notificationEntity: NotificationEntity): Notification {
        return notificationRepository.save(notificationEntity).toModel()
    }
}
