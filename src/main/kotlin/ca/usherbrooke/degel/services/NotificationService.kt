package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.ExpoNotificationClient
import ca.usherbrooke.degel.entities.NotificationEntity
import ca.usherbrooke.degel.models.Notification
import ca.usherbrooke.degel.repositories.NotificationRepository
import ca.usherbrooke.degel.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificationService {
    fun notificationRegister(id: UUID, tokenExpo: Notification): Notification
    fun sendNotification(cip: String, title: String, description: String)
}

@Service
class NotificationServiceImpl(private val notificationRepository: NotificationRepository, private val userRepository: UserRepository, private val expoNotificationClient: ExpoNotificationClient) : NotificationService {
    @Transactional
    override fun sendNotification(cip: String, title: String, description: String) {
        val user = userRepository.findByCip(cip)

        if (user != null) {

            val notificationTokens = notificationRepository.findByUserId(user.id!!)

            if (notificationTokens != null) {
                for (notificationToken: NotificationEntity in notificationTokens) {
                    val message =  constructMessage(notificationToken.expoToken, title, description)
                    expoNotificationClient.sendNotification(message)
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

    private fun constructMessage(expoToken: String, title: String, description: String): String {
        return  "{\n" +
                "  \"to\": \"" + expoToken + "\",\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"body\": \"" + description + "\"\n" +
                "}"
    }
}
