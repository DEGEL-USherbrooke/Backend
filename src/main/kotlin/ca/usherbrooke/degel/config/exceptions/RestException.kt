package ca.usherbrooke.degel.config.exceptions

data class RestException(val error: String,
                         val error_description: String)