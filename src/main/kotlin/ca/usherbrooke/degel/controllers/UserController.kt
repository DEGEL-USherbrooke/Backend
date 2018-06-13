package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Permissions.HAS_ADMIN_ROLE
import ca.usherbrooke.degel.config.Permissions.HAS_USER_ROLE
import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.services.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class UserController(val userService: UserService) {

    @PreAuthorize(HAS_USER_ROLE)
    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: UUID): User = userService.getUser(id)

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("/user")
    fun upsertUser(@RequestBody user: User) : Unit = userService.upsertUser(user)
}