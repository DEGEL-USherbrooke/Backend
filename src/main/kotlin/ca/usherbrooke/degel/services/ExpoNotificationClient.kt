package ca.usherbrooke.degel.services

import feign.Headers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Service
@FeignClient(value = "ExpoNotificationClient", url = "https://exp.host/--/api/v2/push/send")
interface ExpoNotificationClient {
    @PostMapping
    @Headers("Content-Type: application/json")
    fun sendNotification(@RequestBody body: String): String
}