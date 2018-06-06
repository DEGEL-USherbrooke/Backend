package ca.usherbrooke.degel.controllers

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/secured")
class SecuredPageController {

    @GetMapping
    fun index(): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.principal != null
                && auth.principal is UserDetails) {
            return "${(auth.principal as UserDetails).username}"
        }
        return ""
    }
}