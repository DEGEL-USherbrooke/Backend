package ca.usherbrooke.degel.entities

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
internal data class UserEntity (
        @Id @GeneratedValue
        val id: UUID

) : ca.usherbrooke.degel.entities.Entity