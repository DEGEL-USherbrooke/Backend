package ca.usherbrooke.degel.entities

import ca.usherbrooke.degel.config.converters.GrantedAuthorityConverter
import ca.usherbrooke.degel.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
        val cip: String,
        val enabled: Boolean,
        val tester: Boolean,
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(
                name="authorities",
                joinColumns=[JoinColumn(name="user_id")]
        )
        @Column(name = "authority")
        @Convert(converter = GrantedAuthorityConverter::class)
        val authorities: Set<GrantedAuthority>
) : DatedEntity() {
    fun toModel(): User = User(
            this.id,
            this.cip,
            this.enabled,
            this.tester)

    companion object {
        fun fromModel(user: User): UserEntity {
            val userEntity = UserEntity(user.cip, user.enabled, user.tester, setOf(SimpleGrantedAuthority("ROLE_USER")))
            return userEntity
        }
    }
}