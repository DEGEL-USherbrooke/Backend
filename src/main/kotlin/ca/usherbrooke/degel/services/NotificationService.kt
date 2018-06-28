package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.NotificationEntity
import ca.usherbrooke.degel.models.Notification
import ca.usherbrooke.degel.models.Value
import ca.usherbrooke.degel.repositories.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificationService {
    fun notificationRegister(id: UUID, token: Notification): Notification
}

@Service
class NotificationServiceImpl(private val notificationRepository: NotificationRepository) : NotificationService {

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
