package ca.usherbrooke.degel.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
        val id: UUID,
        private val username: String,
        private val authorities: Set<GrantedAuthority>,
        private val enabled: Boolean
) : UserDetails {
    companion object {
        const val NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD"
    }

    override fun getAuthorities() = authorities

    override fun isEnabled() = enabled

    override fun getUsername() = username

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = NON_EXISTENT_PASSWORD_VALUE

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}