package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.OnlyId
import ca.usherbrooke.degel.entities.UserEntity
import ca.usherbrooke.degel.exceptions.UserNotFoundException
import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.models.UserDetailsImpl
import ca.usherbrooke.degel.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.streams.toList

interface UserService {
    fun getUser(id: UUID): User
    fun upsertUser(user: User): User
    fun getUsersId(): List<UUID>
}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService, UserDetailsService {
    @Transactional
    override fun getUsersId() = userRepository.findAllProjectedBy().stream().map(OnlyId::getId).toList()

    @Transactional
    @Throws(UserNotFoundException::class)
    override fun getUser(id: UUID): User = userRepository.findById(id)
            .map { u -> u.toModel() }
            .orElseThrow { UserNotFoundException(id) }

    @Transactional
    override fun upsertUser(user: User): User {
        return upsertUserEntity(user).toModel()
    }

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = upsertUserEntity(User(null, username, true))

        // The toSet is necessary to detach the set from Hibernate persistence cache
        return UserDetailsImpl(userEntity.id!!, userEntity.cip, userEntity.authorities.toSet(), userEntity.enabled)
    }

    @Transactional
    private fun upsertUserEntity(user: User): UserEntity {
        var userEntity = userRepository.findByCip(user.cip)

        if(userEntity == null) {
            userEntity = UserEntity.fromModel(user)
        }

        return userRepository.save(userEntity)
    }
}