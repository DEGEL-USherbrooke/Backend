package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.exceptions.RestException
import ca.usherbrooke.degel.exceptions.ServerSideException
import ca.usherbrooke.degel.models.AuthorizationCode
import ca.usherbrooke.degel.models.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class AuthController {
    @GetMapping("/oauth/error")
    fun error(request: HttpServletRequest): RestException {
        val error = request.getAttribute("error")

        if (error is OAuth2Exception) {
            return RestException(error.oAuth2ErrorCode, error.message.orEmpty())
        }

        return RestException("oauth_error", "OAuth error")
    }

    @GetMapping("/oauth/callback")
    fun callback(@RequestParam("code") code: String,
                 @RequestParam("state") state: String): AuthorizationCode {
        return AuthorizationCode(code, state)
    }

    @GetMapping("/api/whoami")
    fun whoami(): Value<String> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.principal != null && auth.principal is UserDetails) {
            return Value((auth.principal as UserDetails).username)
        }
        throw ServerSideException("No user authenticated")
    }
}