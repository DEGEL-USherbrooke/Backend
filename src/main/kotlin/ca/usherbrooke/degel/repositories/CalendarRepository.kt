package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.CalendarEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface CalendarRepository : JpaRepository<CalendarEntity, UUID> {
    fun findByUserId(userId: UUID): CalendarEntity?
}