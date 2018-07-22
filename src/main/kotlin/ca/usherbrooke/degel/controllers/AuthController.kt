package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.exceptions.RestException
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

@RestController
class AuthController(private val tokenStore: TokenStore) {
    companion object {
        private const val BEARER_AUTHENTICATION = "Bearer "
        private const val HEADER_AUTHORIZATION = "authorization"
    }

    @GetMapping("/oauth/error")
    @ApiIgnore
    fun error(request: HttpServletRequest): RestException {
        val error = request.getAttribute("error")

        if (error is OAuth2Exception) {
            return RestException(error.oAuth2ErrorCode, error.message.orEmpty())
        }

        return RestException("oauth_error", "OAuth error")
    }

    @GetMapping("/oauth/callback")
    @ApiIgnore
    fun callback(@RequestParam("code") code: String,
                 @RequestParam("state") state: String) = ""

    @GetMapping("/logout/success")
    @ApiIgnore
    fun logoutSuccess() = ""

    @GetMapping("/oauth/logout")
    fun oauthLogout(request: HttpServletRequest) {
        val token: String? = request.getHeader(HEADER_AUTHORIZATION)
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            val oAuth2AccessToken = tokenStore.readAccessToken(token.replace(BEARER_AUTHENTICATION, "").trim())
            if (oAuth2AccessToken != null) {
                val oAuth2RefreshToken = oAuth2AccessToken.refreshToken
                if (oAuth2RefreshToken != null)
                    tokenStore.removeRefreshToken(oAuth2RefreshToken)
                tokenStore.removeAccessToken(oAuth2AccessToken)
            }
        }
    }
}