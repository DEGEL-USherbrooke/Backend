package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.NotificationEntity
import ca.usherbrooke.degel.models.Notification
import ca.usherbrooke.degel.repositories.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificationService {
    fun notificationRegister(id: UUID, token: String): Notification
}

@Service
class NotificationServiceImpl(private val notificationRepository: NotificationRepository) : NotificationService {

    override fun notificationRegister(id: UUID, token: String): Notification {
        val oldToken = notificationRepository.findByExpoToken(token)

        if (oldToken != null) {
            deleteOldToken(oldToken)
        }

        val notificationEntity = NotificationEntity.fromModel(id, Notification(token))
        return addNewToken(notificationEntity)
    }

    @Transactional
    fun deleteOldToken(token: NotificationEntity) {
        notificationRepository.delete(token)
    }

    @Transactional
    fun addNewToken(notificationEntity: NotificationEntity): Notification {
        return notificationRepository.save(notificationEntity).toModel()
    }
}
