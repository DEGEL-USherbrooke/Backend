package ca.usherbrooke.degel.entities

import biweekly.ICalendar
import ca.usherbrooke.degel.config.converters.ICalendarConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "calendar")
data class CalendarEntity(
        val userId: UUID,
        var key: String,
        @Temporal(TemporalType.TIMESTAMP)
        var lastFetch: Date?,
        @Convert(converter = ICalendarConverter::class)
        var calendar: ICalendar?
) : BaseEntity() {
    constructor(userId: UUID, key: String) : this(userId, key, null, null)
}