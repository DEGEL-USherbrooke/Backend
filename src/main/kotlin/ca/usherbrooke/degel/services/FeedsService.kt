package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.FeedEntity
import ca.usherbrooke.degel.models.Feed
import ca.usherbrooke.degel.models.News
import ca.usherbrooke.degel.repositories.FeedsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.util.*
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import java.net.URL


interface FeedsService {
    fun getFeeds(): List<Feed>
    fun upsertFeed(feed: Feed): Feed
    fun getNews(userId: UUID) : List<News>
}

@Service
class FeedsServiceImpl(
        private val feedsRepository: FeedsRepository,
        private val settingsService: SettingsService,
        private val feedsClient: RestTemplate = RestTemplate(),
        @Value("\${app.feeds.max-news-elements}") val maxNewsElements: Int
) : FeedsService {
    @Transactional
    override fun getFeeds(): List<Feed> {
        return feedsRepository.findAll().map { it -> it.toModel() }
    }

    @Transactional
    override fun getNews(userId: UUID) : List<News> {
        val settings = settingsService.getSettings(userId)
        val feeds = feedsRepository.findAllById(settings.feeds)

        val input = SyndFeedInput()
        return feeds.map {
            val response = feedsClient.getForEntity(it.url, Resource::class.java)
            val feed = input.build(XmlReader(response.body!!.inputStream))
            val baseUrl = URL(it.url)
            feed.entries.map {
                val description = Jsoup.clean(it.description.value, Whitelist.none())
                News(
                        it.title,
                        StringEscapeUtils.unescapeHtml(description),
                        "${baseUrl.protocol}://${baseUrl.authority}${it.link}",
                        it.enclosures.getOrNull(0)?.url ?: "",
                        it.publishedDate
                )
            }
        }.flatten().distinctBy {
            it.title
        }.sortedByDescending {
            it.publishedDate
        }.take(maxNewsElements)
    }

    @Transactional
    override fun upsertFeed(feed: Feed): Feed {
        val feedEntity  = FeedEntity.fromModel(feed)
        feedEntity.id = feed.id?.let { feedsRepository.findById(it).orElse(null)?.id }
        return feedsRepository.save(feedEntity).toModel()
    }
}