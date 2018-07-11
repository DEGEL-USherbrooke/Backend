package ca.usherbrooke.degel.clients

import ca.usherbrooke.degel.models.notification.notifyus.NUNotification
import ca.usherbrooke.degel.models.notification.notifyus.NUNotificationType
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "NotifyUsClient", url= "\${app.notification.notifyus.server}")
interface NotifyUsClient {
    @PostMapping("/services/notifications")
    fun sendNotification(@RequestHeader("token") token: String,
                         @RequestBody nuNotification: NUNotification): NUNotification

    @PostMapping("/services/notificationtypes")
    fun registerNotificationType(@RequestHeader("token") token: String,
                                 @RequestBody notificationType: NUNotificationType): NUNotificationType
}