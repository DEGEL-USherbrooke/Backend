package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.NotificationsSettings
import ca.usherbrooke.degel.models.Settings
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "settings")
data class SettingsEntity(
        val userId: UUID,
        val mobileNotifications: Boolean,
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(
                name="feeds_subscriptions",
                joinColumns=[(JoinColumn(name = "user_id", referencedColumnName = "userId"))]
        )
        @Column(name = "feed_id")
        val feeds: Set<UUID>
) : BaseEntity() {
    fun toModel() = Settings(NotificationsSettings(mobileNotifications), feeds)

    companion object {
        fun fromModel(userId: UUID, settings: Settings) = SettingsEntity(
                userId,
                settings.notifications.mobile,
                settings.feeds
        )
    }
}