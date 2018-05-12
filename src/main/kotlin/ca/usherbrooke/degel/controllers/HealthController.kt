package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.services.HealthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthController(val healthService: HealthService){

    @GetMapping("/health")
    fun health() = healthService.getHealth()
}