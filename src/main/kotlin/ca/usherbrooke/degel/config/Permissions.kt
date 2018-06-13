package ca.usherbrooke.degel.config

object Permissions {
    // User
    const val HAS_USER_ROLE: String = "hasRole('USER')"

    // Admin
    const val HAS_ADMIN_ROLE: String = "hasRole('ADMIN')"
    const val OR_HAS_ADMIN_ROLE: String = "or $HAS_ADMIN_ROLE"

    // Trusted client
    const val HAS_TRUSTED_CLIENT_ROLE: String = "hasRole('TRUSTED_CLIENT')"
}