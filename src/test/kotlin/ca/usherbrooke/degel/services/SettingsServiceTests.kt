package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.SettingsEntity
import ca.usherbrooke.degel.models.NotificationsSettings
import ca.usherbrooke.degel.models.Settings
import ca.usherbrooke.degel.repositories.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class SettingsServiceTests {
    companion object {
        val USER_ID = UUID.randomUUID()
        val DEFAULT_USER_SETTINGS = Settings(NotificationsSettings(true), emptySet())
        val CUSTOM_USER_SETTINGS = Settings(NotificationsSettings(false), emptySet())
    }

    val settingsRepositoryMock = mockk<SettingsRepository>()

    val settingsService = SettingsServiceImpl(settingsRepositoryMock)

    @Test
    fun `get user settings`() {
        every { settingsRepositoryMock.findByUserId(USER_ID) } returns SettingsEntity.fromModel(USER_ID, CUSTOM_USER_SETTINGS)

        val settings = settingsService.getSettings(USER_ID)

        assertEquals(CUSTOM_USER_SETTINGS, settings)
    }

    @Test
    fun `when no settings get default settings`() {
        every { settingsRepositoryMock.findByUserId(any()) } returns null

        val settings = settingsService.getSettings(USER_ID)

        assertEquals(DEFAULT_USER_SETTINGS, settings)
    }

    @Test
    fun `if no settings insert them`() {
        val entity = slot<SettingsEntity>()

        every { settingsRepositoryMock.findByUserId(any()) } returns null
        every { settingsRepositoryMock.save(capture(entity)) } answers { entity.captured }

        settingsService.upsertSettings(USER_ID, DEFAULT_USER_SETTINGS)

        verify { settingsRepositoryMock.save(any<SettingsEntity>()) }
        assertNull(entity.captured.id)
    }

    @Test
    fun `if settings present update them`() {
        val entity = slot<SettingsEntity>()

        every { settingsRepositoryMock.findByUserId(any()) } returns SettingsEntity.fromModel(USER_ID, CUSTOM_USER_SETTINGS)
                .also { s -> s.id = UUID.randomUUID() }
        every { settingsRepositoryMock.save(capture(entity)) } answers { entity.captured }

        settingsService.upsertSettings(USER_ID, DEFAULT_USER_SETTINGS)

        verify { settingsRepositoryMock.save(any<SettingsEntity>()) }
        assertNotNull(entity.captured.id)
    }
}