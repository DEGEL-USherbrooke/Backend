package ca.usherbrooke.degel.security

import com.google.common.collect.Lists
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class CustomUserDetailsService : UserDetailsService {
    private val NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD"

    override fun loadUserByUsername(username: String?): UserDetails {
        return User(username, NON_EXISTENT_PASSWORD_VALUE, true, true, true, true, Lists.newArrayList(SimpleGrantedAuthority("ROLE_USER")))
    }

}