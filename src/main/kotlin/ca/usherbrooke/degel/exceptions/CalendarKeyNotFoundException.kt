package ca.usherbrooke.degel.exceptions

import java.util.*

class CalendarKeyNotFoundException(userId: UUID) : ClientSideException("Calendar key not found for user $userId")