package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.FeedEntity
import ca.usherbrooke.degel.models.Feed
import ca.usherbrooke.degel.models.NotificationsSettings
import ca.usherbrooke.degel.models.Settings
import ca.usherbrooke.degel.repositories.FeedsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class FeedsServiceTests {
    companion object {
        val MAX_NEWS_ELEMENTS = 5
        val FEED = Feed(UUID.randomUUID(), "Feed name", "http://perdu.com")
        val OTHER_FEED = Feed(UUID.randomUUID(), "Other name", "http://perdu2.com")
        val UPDATED_FEED = Feed(FEED.id, "Updated name", "http://perdu.com\"")
        val NEW_FEED = Feed(null, "New Feed", "Usherbrooke.ca")
        val USER_ID = UUID.randomUUID()
        val USER_SETTINGS = Settings(NotificationsSettings(true), setOf(FEED.id!!))
    }

    val feedsRepositoryMock = mockk<FeedsRepository>()
    val settingsServiceMock = mockk<SettingsService>()
    val feedsClientMock = mockk<RestTemplate>()
    val feedsService = FeedsServiceImpl(feedsRepositoryMock, settingsServiceMock, feedsClientMock, MAX_NEWS_ELEMENTS)

    @Test
    fun `get all feeds`() {
        val feedEntity = FeedEntity.fromModel(FEED)
        feedEntity.id = FEED.id

        every { feedsRepositoryMock.findAll() } returns listOf(feedEntity)

        val feeds = feedsService.getFeeds()

        assertEquals(feeds[0], FEED)
    }

    @Test
    fun `create new feed if not exists`() {
        val entity = slot<FeedEntity>()

        every { feedsRepositoryMock.findById(any()) } returns Optional.empty()
        every { feedsRepositoryMock.save(capture(entity)) } answers { entity.captured }

        feedsService.upsertFeed(NEW_FEED)

        verify { feedsRepositoryMock.save(any<FeedEntity>()) }
        assertNull(entity.captured.id)
    }

    @Test
    fun `update existing feed if exists`() {
        val feedEntity = FeedEntity.fromModel(FEED)
        feedEntity.id = FEED.id
        val entity = slot<FeedEntity>()

        every { feedsRepositoryMock.findById(any()) } returns Optional.of(feedEntity)
        every { feedsRepositoryMock.save(capture(entity)) } answers { entity.captured }

        feedsService.upsertFeed(UPDATED_FEED)

        verify { feedsRepositoryMock.save(any<FeedEntity>()) }
        assertEquals(entity.captured.id, FEED.id)
        assertEquals(entity.captured.name, UPDATED_FEED.name)
    }

    @Test
    fun `get news for user`() {
        val response = ResponseEntity<Resource>(
                InputStreamResource(this::class.java.getResourceAsStream("/feeds/law.xml")),
                HttpStatus.OK
        )
        val feedEntity = FeedEntity.fromModel(FEED)
        feedEntity.id = FEED.id

        every { settingsServiceMock.getSettings(USER_ID) } returns USER_SETTINGS
        every { feedsRepositoryMock.findAllById(setOf(FEED.id!!)) } returns listOf(feedEntity)
        every { feedsClientMock.getForEntity(any<String>(), Resource::class.java) } returns response

        val news = feedsService.getNews(USER_ID)

        assertEquals(news.size, MAX_NEWS_ELEMENTS)
        assertEquals(news[0].publishedDate.toInstant(), Instant.ofEpochMilli(1541566800000))
    }

    @Test
    fun `get news for user without duplicates`() {
        val response = ResponseEntity<Resource>(
                InputStreamResource(this::class.java.getResourceAsStream("/feeds/law.xml")),
                HttpStatus.OK
        )
        val otherResponse = ResponseEntity<Resource>(
                InputStreamResource(this::class.java.getResourceAsStream("/feeds/law.xml")),
                HttpStatus.OK
        )
        val feedEntity = FeedEntity.fromModel(FEED)
        feedEntity.id = FEED.id
        val otherFeedEntity = FeedEntity.fromModel(OTHER_FEED)
        otherFeedEntity.id = OTHER_FEED.id

        every { settingsServiceMock.getSettings(USER_ID) } returns USER_SETTINGS
        every { feedsRepositoryMock.findAllById(setOf(FEED.id!!)) } returns listOf(feedEntity, otherFeedEntity)
        every { feedsClientMock.getForEntity(FEED.url, Resource::class.java) } returns response
        every { feedsClientMock.getForEntity(OTHER_FEED.url, Resource::class.java) } returns otherResponse

        val news = feedsService.getNews(USER_ID)

        assertEquals(news.size, MAX_NEWS_ELEMENTS)
        assertEquals(news[0].publishedDate.toInstant(), Instant.ofEpochMilli(1541566800000))
        assertEquals(1, news.count {
            it.title == "Créer des ponts entre autochtones et allochtones une initiative à la fois"
        })
    }
}