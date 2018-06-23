package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.NotificationsSettings
import ca.usherbrooke.degel.models.Settings
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "settings")
data class SettingsEntity(val userId: UUID,
                          val mobileNotifications: Boolean
) : BaseEntity() {
    fun toModel() = Settings(NotificationsSettings(mobileNotifications))

    companion object {
        fun fromModel(userId: UUID, settings: Settings) = SettingsEntity(userId, settings.notifications.mobile)
    }
}