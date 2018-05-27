package ca.usherbrooke.degel.exceptions

import java.util.*

class UserNotFoundException(userId: UUID) : ClientSideException("User $userId does not exist")