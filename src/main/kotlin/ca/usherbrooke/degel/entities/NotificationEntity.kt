package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.Notification
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "notification_token")
data class NotificationEntity(val userId: UUID, val expoToken: String) : BaseEntity() {
    fun toModel() = Notification(expoToken)

    companion object {
        fun fromModel(userId: UUID, notification: Notification) = NotificationEntity(userId, notification.expoToken)
    }
}