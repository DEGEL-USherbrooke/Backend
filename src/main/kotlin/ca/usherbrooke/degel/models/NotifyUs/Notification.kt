package ca.usherbrooke.degel.models.NotifyUs

data class Notification(val cip: String,
                        val type: String,
                        val title: String,
                        val description: String)