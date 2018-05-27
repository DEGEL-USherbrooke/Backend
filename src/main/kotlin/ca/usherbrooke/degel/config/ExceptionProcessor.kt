package ca.usherbrooke.degel.config

import ca.usherbrooke.degel.exceptions.ClientSideException
import ca.usherbrooke.degel.exceptions.ServerSideException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

data class RestException(val message: String)

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionProcessor {
    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun noHandlerFoundException() = RestException("Not found")

    @ExceptionHandler(ClientSideException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun clientSideException(e: ClientSideException) = RestException(e.message.orEmpty())

    @ExceptionHandler(ServerSideException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun serverSideException(e: ServerSideException) = RestException("Server side exception")

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun unexpectedException(e: Exception) : RestException {
        logger.error("Unexpected error", e)
        return RestException("Unexpected error")
    }
}