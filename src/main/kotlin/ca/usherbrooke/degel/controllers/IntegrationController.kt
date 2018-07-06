package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Constants
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.exceptions.FailedRegisterNUNotificationTypeException
import ca.usherbrooke.degel.exceptions.FailedSendNUNotificationException
import ca.usherbrooke.degel.models.notification.notifyus.NUNotification
import ca.usherbrooke.degel.models.notification.notifyus.NUNotificationType
import ca.usherbrooke.degel.services.IntegrationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping(Constants.API)
@ApiIgnore
class IntegrationController(val service : IntegrationService) {

    @PreAuthorize(Permissions.HAS_ADMIN_ROLE)
    @PostMapping("/notification/notifyus/register")
    @Throws(FailedRegisterNUNotificationTypeException::class)
    fun registerNotifyUsNotification(@RequestBody notificationType: NUNotificationType) = service.registerNotifyUsNotificationType(notificationType)

    @PreAuthorize(Permissions.HAS_ADMIN_ROLE)
    @PostMapping("/nuNotification/notifyus/send")
    @Throws(FailedSendNUNotificationException::class)
    fun sendNotifyUsNotification(@RequestBody nuNotification: NUNotification) = service.sendNotifyUsNotification(nuNotification)
}