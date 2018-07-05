package ca.usherbrooke.degel.clients

import feign.Headers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Service
@FeignClient(value = "ExpoNotificationClient", url = "\${app.notification.expo-server}")
interface ExpoNotificationClient {
    @PostMapping
    @Headers("Content-Type: application/json")
    fun sendNotification(@RequestBody body: String): String
}