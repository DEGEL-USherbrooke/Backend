package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.FeedEntity
import ca.usherbrooke.degel.models.Feed
import ca.usherbrooke.degel.repositories.FeedsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface FeedsService {
    fun getFeeds(): List<Feed>
    fun upsertFeed(feed: Feed): Feed
}

@Service
class FeedsServiceImpl(private val feedsRepository: FeedsRepository) : FeedsService {
    @Transactional
    override fun getFeeds(): List<Feed> {
        return feedsRepository.findAll().map { it -> it.toModel() }
    }

    @Transactional
    override fun upsertFeed(feed: Feed): Feed {
        val feed  = FeedEntity.fromModel(feed)
        feed.id = feed.id?.let { feedsRepository.findById(it).orElse(null)?.id }
        return feedsRepository.save(feed).toModel()
    }
}