package ca.usherbrooke.degel.services

import ca.usherbrooke.degel.exceptions.UserNotFoundException
import ca.usherbrooke.degel.models.User
import ca.usherbrooke.degel.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserService {
    fun getUser(id: UUID): User
}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    @Transactional
    @Throws(UserNotFoundException::class)
    override fun getUser(id: UUID): User = userRepository.findById(id)
            .map { u -> u.toModel() }
            .orElseThrow { UserNotFoundException(id) }
}