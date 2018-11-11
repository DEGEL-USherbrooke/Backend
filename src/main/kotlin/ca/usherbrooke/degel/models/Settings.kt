package ca.usherbrooke.degel.models

import java.util.*

data class NotificationsSettings(val mobile: Boolean)

data class Settings(
        val notifications: NotificationsSettings,
        val feeds: Set<UUID>
)