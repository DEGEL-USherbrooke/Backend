package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

interface NotificationRepository : JpaRepository<NotificationEntity, UUID> {
    @Transactional
    fun findByExpoToken(tokenExpo: String): NotificationEntity?
}