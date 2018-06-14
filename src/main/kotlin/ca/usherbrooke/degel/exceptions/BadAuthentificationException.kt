package ca.usherbrooke.degel.exceptions

class BadAuthentificationException(user: Any) : ServerSideException("User is not authenticated correctly: $user")