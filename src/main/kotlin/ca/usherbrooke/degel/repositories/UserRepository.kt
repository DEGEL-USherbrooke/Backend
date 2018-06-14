package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByCip(cip: String): UserEntity?
}