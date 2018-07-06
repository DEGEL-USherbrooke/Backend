package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.notification.NotificationToken
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "notification_token")
data class NotificationTokenEntity(val userId: UUID, val expoToken: String) : BaseEntity() {
    fun toModel() = NotificationToken(expoToken)

    companion object {
        fun fromModel(userId: UUID, notificationToken: NotificationToken) = NotificationTokenEntity(userId, notificationToken.expoToken)
    }
}