package ca.usherbrooke.degel.exceptions

import java.util.*

class CalendarCouldNotBeFetchedException(userId: UUID, e: Throwable
) : ServerSideException("Calendar could not be fetched from server for user $userId", e)