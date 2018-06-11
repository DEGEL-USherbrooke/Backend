package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class UserController(val userService: UserService) {

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: UUID): User = userService.getUser(id)

    @PostMapping("/user")
    fun upsertUser(@RequestBody user: User) : Unit = userService.upsertUser(user)
}