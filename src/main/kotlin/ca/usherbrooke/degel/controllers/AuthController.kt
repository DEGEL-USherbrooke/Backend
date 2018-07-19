package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.exceptions.RestException
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

@RestController
@ApiIgnore
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
                 @RequestParam("state") state: String) = ""

    @GetMapping("/logout/success")
    fun logoutSuccess() = ""
}