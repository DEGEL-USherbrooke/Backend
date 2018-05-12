package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.models.Health

interface HealthService {
    fun getHealth() : Health
}

class HealthServiceImpl : HealthService {
    override fun getHealth() = Health(isAlive = true)
}