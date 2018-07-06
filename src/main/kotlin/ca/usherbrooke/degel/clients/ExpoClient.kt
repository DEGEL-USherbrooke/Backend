package ca.usherbrooke.degel.clients

import ca.usherbrooke.degel.models.notification.ExpoNotification
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "ExpoClient", url = "\${app.notification.expo-server}")
interface ExpoClient {
    @PostMapping
    @Headers("Content-Type: application/json")
    fun sendNotification(@RequestBody body: ExpoNotification): String
}