package ca.usherbrooke.degel.services

import com.google.common.collect.Lists
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(val userService: UserService) : UserDetailsService {
    private val NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD"

    override fun loadUserByUsername(username: String): UserDetails {
        userService.upsertUser(ca.usherbrooke.degel.models.User(null, username, null))

        return User(username, NON_EXISTENT_PASSWORD_VALUE, true, true, true, true, Lists.newArrayList(SimpleGrantedAuthority("ROLE_USER")))
    }

}