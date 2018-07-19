package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.clients.ExpoClient
import ca.usherbrooke.degel.entities.NotificationTokenEntity
import ca.usherbrooke.degel.models.notification.NotificationToken
import ca.usherbrooke.degel.repositories.NotificationTokenRepository
import ca.usherbrooke.degel.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationServiceTests {
    companion object {
        val USER_ID = UUID.randomUUID()
        val EXPO_TOKEN = NotificationToken("Token")
        val EXPO_TOKEN1 = NotificationToken("Token1")
        val NOTIFICATION = NotificationToken("Token")
    }

    private val notificationRepositoryMock = mockk<NotificationTokenRepository>()
    private val userRepositoryMock = mockk<UserRepository>()
    private val expoClientMock = mockk<ExpoClient>()
    private val settingsService = mockk<SettingsService>()
    private val notificationService = NotificationServiceImpl(notificationRepositoryMock,
            userRepositoryMock,
            settingsService,
            expoClientMock)

    @Test
    fun `when new token add token`() {
        val entity = slot<NotificationTokenEntity>()

        every { notificationRepositoryMock.findByExpoToken(any()) } returns null
        every { notificationRepositoryMock.save(capture(entity)) } answers { entity.captured }

        val notificationToken = notificationService.notificationRegister(USER_ID, EXPO_TOKEN)

        assertEquals(NOTIFICATION, notificationToken)
    }

    @Test
    fun `when duplicate token delete old token and then add new token`() {
        val entityOldToken = slot<NotificationTokenEntity>()
        val entityNewToken = slot<NotificationTokenEntity>()

        every { notificationRepositoryMock.findByExpoToken(any()) } returns NotificationTokenEntity(USER_ID, EXPO_TOKEN1.expoToken)
        every { notificationRepositoryMock.delete(capture(entityOldToken)) } returns Unit
        every { notificationRepositoryMock.save(capture(entityNewToken)) } answers { entityNewToken.captured }

        val notificationToken = notificationService.notificationRegister(USER_ID, EXPO_TOKEN)

        assertEquals(NOTIFICATION, notificationToken)
        assertEquals(entityOldToken.captured, NotificationTokenEntity(USER_ID, EXPO_TOKEN1.expoToken))
    }
}