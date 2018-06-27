package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Constants.API
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.models.Value
import ca.usherbrooke.degel.services.NotificationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(API)
class NotificationController(val service : NotificationService) {

    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @PostMapping("/user/{id}/notification/register")
    fun notificationRegister(@PathVariable id: UUID, @RequestBody body: String) = service.notificationRegister(id, body)
}