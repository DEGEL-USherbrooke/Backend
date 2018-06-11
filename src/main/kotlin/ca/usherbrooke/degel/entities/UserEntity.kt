package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.User
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
data class UserEntity(
        val cip: String,
        val token: String?
) : BaseEntity() {
    fun toModel(): User = User(
            this.id,
            this.cip,
            this.token)

    companion object {
        fun fromModel(user: User) = UserEntity(user.cip, user.token)
    }
}