package ca.usherbrooke.degel.models.notification

import java.util.*

// Must have either a CIP or a userId
data class Notification(val cip: String?,
                        val userId: UUID?,
                        val title: String,
                        val description: String)