package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.exceptions.RestException
import ca.usherbrooke.degel.exceptions.ServerSideException
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ApiIgnore
class ErrorController : ErrorController {
    companion object {
        const val PATH = "/error"
    }

    @GetMapping(PATH)
    fun getError(request: HttpServletRequest, response: HttpServletResponse): RestException {
        return RestException(ServerSideException.SERVER_SIDE_EXCEPTION, "Server side exception")
    }

    override fun getErrorPath(): String {
        return PATH
    }
}