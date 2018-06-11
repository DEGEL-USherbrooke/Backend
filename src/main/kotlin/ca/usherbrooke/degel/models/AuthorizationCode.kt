package ca.usherbrooke.degel.models

data class AuthorizationCode(val code: String,
                             val state: String)