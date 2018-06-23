package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.SettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface SettingsRepository : JpaRepository<SettingsEntity, UUID> {
    fun findByUserId(userId: UUID): SettingsEntity?
}