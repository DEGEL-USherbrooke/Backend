package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.models.Feed
import javax.persistence.*;

@Entity
@Table(name= "feeds")
data class FeedEntity(
        val name: String,
        val url: String
) : DatedEntity() {
    fun toModel() = Feed(id, name, url)

    companion object {
        fun fromModel(feed: Feed) = FeedEntity(feed.name, feed.url)
    }
}