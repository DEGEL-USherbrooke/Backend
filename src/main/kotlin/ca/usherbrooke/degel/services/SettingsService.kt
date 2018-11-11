package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.SettingsEntity
import ca.usherbrooke.degel.models.NotificationsSettings
import ca.usherbrooke.degel.models.Settings
import ca.usherbrooke.degel.repositories.SettingsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface SettingsService {
    fun getSettings(userId: UUID) : Settings
    fun upsertSettings(userId: UUID, settings: Settings) : Settings
}

@Service
class SettingsServiceImpl(private val settingsRepository: SettingsRepository) : SettingsService {
    @Transactional
    override fun getSettings(userId: UUID) : Settings {
       return settingsRepository.findByUserId(userId)?.toModel() ?: createDefaultSettings()
    }

    @Transactional
    override fun upsertSettings(userId: UUID, settings: Settings) : Settings {
        val settingsEntity = SettingsEntity.fromModel(userId, settings)
        settingsEntity.id = settingsRepository.findByUserId(userId)?.id
        return settingsRepository.save(settingsEntity).toModel()
    }

    private fun createDefaultSettings() = Settings(NotificationsSettings(true), emptySet())
}