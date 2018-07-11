package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.NotificationTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

interface NotificationTokenRepository : JpaRepository<NotificationTokenEntity, UUID> {
    @Transactional
    fun findByExpoToken(tokenExpo: String): NotificationTokenEntity?

    @Transactional
    fun findByUserId(userId: UUID): List<NotificationTokenEntity>?
}