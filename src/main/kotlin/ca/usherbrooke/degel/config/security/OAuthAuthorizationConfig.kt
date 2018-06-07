package ca.usherbrooke.degel.config.security

import ca.usherbrooke.degel.config.exceptions.OauthExceptionTranslator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer

@Configuration
@EnableAuthorizationServer
class OAuthAuthorizationConfig(
        val authenticationManager: AuthenticationManager,
        @Value("\${app.location}") val location: String,
        @Value("\${app.security.access-token-validity}") val accessTokenValidity: Int,
        @Value("\${app.security.refresh-token-validity}") val refreshTokenValidity: Int
) : AuthorizationServerConfigurerAdapter() {
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("SampleClientId")
                .secret("{noop}")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("user_info")
                .redirectUris("$location/oauth/callback")
                .refreshTokenValiditySeconds(refreshTokenValidity)
                .accessTokenValiditySeconds(accessTokenValidity)
                .autoApprove(true)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
                .exceptionTranslator(OauthExceptionTranslator())
    }
}