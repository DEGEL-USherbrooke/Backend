package ca.usherbrooke.degel.entities

import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id @GeneratedValue
    var id: UUID? = null
}

interface OnlyId {
    fun getId(): UUID
}