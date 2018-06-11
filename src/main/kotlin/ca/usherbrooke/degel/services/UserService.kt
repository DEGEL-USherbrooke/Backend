package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.entities.UserEntity
import ca.usherbrooke.degel.exceptions.UserNotFoundException
import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserService {
    fun getUser(id: UUID): User
    fun upsertUser(user: User): Unit
}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    @Transactional
    @Throws(UserNotFoundException::class)
    override fun getUser(id: UUID): User = userRepository.findById(id)
            .map { u -> u.toModel() }
            .orElseThrow { UserNotFoundException(id) }

    @Transactional
    override fun upsertUser(user: User): Unit {
        var userEntity = userRepository.findByCip(user.cip)

        if(userEntity == null) {
            userEntity = UserEntity.fromModel(user)
        }

        userRepository.save(userEntity)
    }
}