package ca.usherbrooke.degel.config.security

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator
import org.jasig.cas.client.validation.TicketValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.cas.ServiceProperties
import org.springframework.security.cas.authentication.CasAuthenticationProvider
import org.springframework.security.cas.web.CasAuthenticationEntryPoint
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint

@Configuration
class CasAuthorizationConfig(
        val userDetailsService: UserDetailsService,
        @Value("\${app.location}") val location: String,
        @Value("\${app.security.cas-server}") val casServer: String
) {
    @Bean
    fun serviceProperties(): ServiceProperties {
        val serviceProperties = ServiceProperties()
        serviceProperties.service = "$location/login/cas"
        serviceProperties.isSendRenew = false
        return serviceProperties
    }

    @Bean
    @Primary
    fun authenticationEntryPoint(
            sP: ServiceProperties): AuthenticationEntryPoint {

        val entryPoint = CasAuthenticationEntryPoint()
        entryPoint.loginUrl = "$casServer/login"
        entryPoint.serviceProperties = sP
        return entryPoint
    }

    @Bean
    fun ticketValidator(): TicketValidator {
        return Cas20ServiceTicketValidator(casServer)
    }

    @Bean
    fun casAuthenticationProvider(): CasAuthenticationProvider {

        val provider = CasAuthenticationProvider()
        provider.setServiceProperties(serviceProperties())
        provider.setTicketValidator(ticketValidator())
        provider.setUserDetailsService(userDetailsService)
        provider.setKey("CAS_PROVIDER_USHERBROOKE")
        return provider
    }
}