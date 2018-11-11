package ca.usherbrooke.degel.models

import java.util.*

data class News(
        val title: String,
        val description: String,
        val link: String,
        val imageUrl: String,
        val publishedDate: Date
)