package ca.usherbrooke.degel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DegelApplication

fun main(args: Array<String>) {
    runApplication<DegelApplication>(*args)
}
