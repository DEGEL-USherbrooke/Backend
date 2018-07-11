package ca.usherbrooke.degel.config

object Permissions {
    // User
    const val HAS_USER_ROLE: String = "hasRole('USER')"
    const val USER_OWN_RESSOURCE : String = "($HAS_USER_ROLE and #id == principal.id)"

    // Admin
    const val HAS_ADMIN_ROLE: String = "hasRole('ADMIN')"

    // Trusted client
    const val HAS_TRUSTED_CLIENT_ROLE: String = "hasRole('TRUSTED_CLIENT')"
    const val TRUSTED_NOTIFICATIONS_PROVIDER: String = "($HAS_TRUSTED_CLIENT_ROLE and #oauth2.hasScope('push_notification'))"
}