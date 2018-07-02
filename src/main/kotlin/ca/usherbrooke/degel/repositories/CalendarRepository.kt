package ca.usherbrooke.degel.repositories

import biweekly.ICalendar
import ca.usherbrooke.degel.entities.CalendarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface CalendarRepository : JpaRepository<CalendarEntity, UUID> {
    fun findByUserId(userId: UUID): CalendarEntity?

    @Modifying
    @Query("UPDATE #{#entityName} SET calendar = :calendar, lastFetch = :fetchDate WHERE userId = :userId")
    fun setCalendar(@Param("calendar") calendar: ICalendar,
                    @Param("fetchDate") fetchDate: Date,
                    @Param("userId") userId: UUID)
}