package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
class UserController(val userService: UserService) {

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: UUID): User = userService.getUser(id)
}