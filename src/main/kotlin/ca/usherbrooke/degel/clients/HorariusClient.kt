package ca.usherbrooke.degel.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "HorariusClient", url= "\${app.calendar.horarius}")
interface HorariusClient {
    @GetMapping("/icalendar")
    fun getCalendar(@RequestParam("key") key: String): String
}