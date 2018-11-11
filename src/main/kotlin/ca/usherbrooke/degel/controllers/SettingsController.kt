package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Constants.API
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.exceptions.BadBodyException
import ca.usherbrooke.degel.exceptions.DegelException
import ca.usherbrooke.degel.models.Settings
import ca.usherbrooke.degel.services.SettingsService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(API)
class SettingsController(val settingsService: SettingsService) {
    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @GetMapping("/user/{id}/settings")
    fun getSettings(@PathVariable id: UUID) = settingsService.getSettings(id)

    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @PostMapping("/user/{id}/settings")
    @Throws(DegelException::class)
    fun upsertSettings(@PathVariable id: UUID, @RequestBody settings: Settings) : Settings {
        try {
            return settingsService.upsertSettings(id, settings)
        } catch (e: DataIntegrityViolationException) {
            throw BadBodyException("Wrong feeds specified")
        }
    }
}