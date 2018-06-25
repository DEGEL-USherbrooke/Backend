package ca.usherbrooke.degel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableResourceServer
@EnableFeignClients
@EnableScheduling
class DegelApplication

fun main(args: Array<String>) {
    runApplication<DegelApplication>(*args)
}
