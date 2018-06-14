package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.models.Health
import org.springframework.stereotype.Service

interface HealthService {
    fun getHealth() : Health
}

@Service
class HealthServiceImpl : HealthService {
    override fun getHealth() = Health(isAlive = true)
}