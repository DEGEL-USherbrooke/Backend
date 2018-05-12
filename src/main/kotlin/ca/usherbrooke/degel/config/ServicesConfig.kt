package ca.usherbrooke.degel.config

import ca.usherbrooke.degel.services.HealthServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServicesConfig {
    @Bean
    fun healthService() = HealthServiceImpl()
}