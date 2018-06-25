package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.services.NotificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notification")
class NotificationController(val service : NotificationService) {

    @GetMapping("/NotificationService")
    fun health() = service.validateClient("TITLE", "DESCRIPTION")
}