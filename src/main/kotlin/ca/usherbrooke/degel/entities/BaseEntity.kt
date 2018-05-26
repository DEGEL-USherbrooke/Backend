package ca.usherbrooke.degel.entities

import java.util.*
import javax.persistence.*


@MappedSuperclass
abstract class Entity(
        @Temporal(TemporalType.TIMESTAMP)
        private var createdAt: Date = Date(),

        @Temporal(TemporalType.TIMESTAMP)
        private var updatedAt: Date = Date()
) {
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