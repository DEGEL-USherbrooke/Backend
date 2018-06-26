package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.NotificationEntity
import ca.usherbrooke.degel.models.Notification
import ca.usherbrooke.degel.repositories.NotificationRepository
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
        val EXPO_TOKEN = "Token"
        val EXPO_TOKEN1 = "Token1"
        val NOTIFICATION = Notification(EXPO_TOKEN)
    }

    private val notificationRepositoryMock = mockk<NotificationRepository>()
    private val notificationService = NotificationServiceImpl(notificationRepositoryMock)

    @Test
    fun `when new token add token`() {
        val entity = slot<NotificationEntity>()

        every { notificationRepositoryMock.findByExpoToken(any()) } returns null
        every { notificationRepositoryMock.save(capture(entity)) } answers { entity.captured }

        val notificationToken = notificationService.notificationRegister(USER_ID, EXPO_TOKEN)

        assertEquals(NOTIFICATION, notificationToken)
    }

    @Test
    fun `when duplicate token delete old token and then add new token`() {
        val entityOldToken = slot<NotificationEntity>()
        val entityNewToken = slot<NotificationEntity>()

        every { notificationRepositoryMock.findByExpoToken(any()) } returns NotificationEntity(USER_ID, EXPO_TOKEN1)
        every { notificationRepositoryMock.delete(capture(entityOldToken)) } returns Unit
        every { notificationRepositoryMock.save(capture(entityNewToken)) } answers { entityNewToken.captured }

        val notificationToken = notificationService.notificationRegister(USER_ID, EXPO_TOKEN)

        assertEquals(NOTIFICATION, notificationToken)
        assertEquals(entityOldToken.captured, NotificationEntity(USER_ID, EXPO_TOKEN1))
    }
}