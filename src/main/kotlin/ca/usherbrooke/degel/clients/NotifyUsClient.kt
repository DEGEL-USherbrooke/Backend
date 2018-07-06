package ca.usherbrooke.degel.clients

import ca.usherbrooke.degel.models.NotifyUs.Notification
import ca.usherbrooke.degel.models.NotifyUs.NotificationType
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "NotifyUsClient", url= "\${app.notification.notifyus.service}")
interface NotifyUsClient {
    @PostMapping("/services/notifications")
    fun sendNotification(@RequestHeader("token") token: String,
                         @RequestBody notification: Notification): Notification

    @PostMapping("/services/notificationtypes")
    fun registerNotificationType(@RequestHeader("token") token: String,
                                 @RequestBody notificationType: NotificationType): NotificationType
}