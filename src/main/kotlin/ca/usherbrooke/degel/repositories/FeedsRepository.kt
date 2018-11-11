package ca.usherbrooke.degel.repositories

import ca.usherbrooke.degel.entities.FeedEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional(Transactional.TxType.MANDATORY)
interface FeedsRepository : JpaRepository<FeedEntity, UUID>