package ca.usherbrooke.degel.exceptions

import java.util.*

class CalendarKeyNotFoundException(userId: UUID) : ClientSideException("Calendar key not found for user $userId")

class CalendarKeyInvalidException(userId: UUID) : ClientSideException("Calendar key of user $userId is invalid")

class CalendarCouldNotBeFetchedException(userId: UUID, e: Throwable
) : ServerSideException("Calendar could not be fetched from server for user $userId", e)