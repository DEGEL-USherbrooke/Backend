package ca.usherbrooke.degel.models.notification.notifyus

data class NUNotification(val cip: String,
                          val type: String,
                          val title: String,
                          val description: String)