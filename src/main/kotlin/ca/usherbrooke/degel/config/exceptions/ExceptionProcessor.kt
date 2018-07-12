package ca.usherbrooke.degel.config.exceptions

import ca.usherbrooke.degel.exceptions.ClientSideException
import ca.usherbrooke.degel.exceptions.ServerSideException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionProcessor {
    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun noHandlerFoundException() = RestException("not_found", "Not found")

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun accessDeniedException(e: AccessDeniedException) = RestException("access_denied", "Access denied")

    @ExceptionHandler(ClientSideException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun clientSideException(e: ClientSideException) : RestException {
        logger.error("Client side error: ${e.message}")
        return RestException(e.error, e.message.orEmpty())
    }

    @ExceptionHandler(ServerSideException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun serverSideException(e: ServerSideException) : RestException {
        logger.error("Server side error: ${e.message}")
        return RestException(e.error, "Server side exception")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun unexpectedException(e: Exception) : RestException {
        logger.error("Unexpected error", e)
        return RestException("unexpected_error", "Unexpected error")
    }
}