package ca.usherbrooke.degel.entities

import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class DatedEntity : BaseEntity() {
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date? = null

    @PrePersist
    protected fun onCreate() {
        this.createdAt = Date()
        this.updatedAt = this.createdAt
    }

    @PreUpdate
    protected fun onUpdate() {
        this.updatedAt = Date()
    }
}