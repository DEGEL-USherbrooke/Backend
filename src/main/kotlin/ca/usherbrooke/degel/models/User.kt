package ca.usherbrooke.degel.models

import java.util.*

data class User(
        val id: UUID,
        val cip: String,
        val token: String?
)