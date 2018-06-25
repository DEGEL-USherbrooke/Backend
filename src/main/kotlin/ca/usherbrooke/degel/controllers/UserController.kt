package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Permissions.HAS_ADMIN_ROLE
import ca.usherbrooke.degel.config.Permissions.HAS_USER_ROLE
import ca.usherbrooke.degel.config.Permissions.USER_OWN_RESSOURCE
import ca.usherbrooke.degel.exceptions.BadAuthentificationException
import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.models.UserDetailsImpl
import ca.usherbrooke.degel.services.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class UserController(val userService: UserService) {

    @PreAuthorize("$USER_OWN_RESSOURCE or $HAS_ADMIN_ROLE")
    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: UUID): User = userService.getUser(id)

    @PreAuthorize(HAS_USER_ROLE)
    @GetMapping("/user/current")
    fun getCurrentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal
        if (principal is UserDetailsImpl) {
            return userService.getUser(principal.id)
        }

        // Should not happen
        throw BadAuthentificationException(principal)
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("/user")
    fun upsertUser(@RequestBody user: User) : User = userService.upsertUser(user)

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/user")
    fun getAllUsers() = userService.getUsersId()
}