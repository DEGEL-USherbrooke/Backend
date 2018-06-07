package ca.usherbrooke.degel.config.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator

class OauthExceptionTranslator : WebResponseExceptionTranslator<OAuth2Exception> {
    override fun translate(e: Exception): ResponseEntity<OAuth2Exception> {
        val oAuth2Exception = OAuth2Exception("Invalid autorization request")

        return ResponseEntity(oAuth2Exception, HttpStatus.BAD_REQUEST)
    }
}