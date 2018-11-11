package ca.usherbrooke.degel.entities

import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity : Serializable {
    @Id @GeneratedValue
    var id: UUID? = null
}

interface OnlyId {
    fun getId(): UUID
}