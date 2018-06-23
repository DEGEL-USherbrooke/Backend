package ca.usherbrooke.degel.models

data class NotificationsSettings(val mobile: Boolean)

data class Settings(val notifications: NotificationsSettings)