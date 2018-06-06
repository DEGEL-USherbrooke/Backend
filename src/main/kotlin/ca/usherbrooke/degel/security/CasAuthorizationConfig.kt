package ca.usherbrooke.degel.security

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.cas.authentication.CasAuthenticationProvider
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator
import org.jasig.cas.client.validation.TicketValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.cas.web.CasAuthenticationEntryPoint
import org.springframework.security.cas.ServiceProperties
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class CasAuthorizationConfig() {
    @Bean
    fun serviceProperties(): ServiceProperties {
        val serviceProperties = ServiceProperties()
        serviceProperties.service = "http://localhost:8080/login/cas"
        serviceProperties.isSendRenew = false
        return serviceProperties
    }

    @Bean
    @Primary
    fun authenticationEntryPoint(
            sP: ServiceProperties): AuthenticationEntryPoint {

        val entryPoint = CasAuthenticationEntryPoint()
        entryPoint.loginUrl = "https://cas.usherbrooke.ca/login"
        entryPoint.serviceProperties = sP
        return entryPoint
    }

    @Bean
    fun ticketValidator(): TicketValidator {
        return Cas20ServiceTicketValidator(
                "https://cas.usherbrooke.ca")
    }

    @Bean
    fun casAuthenticationProvider(): CasAuthenticationProvider {

        val provider = CasAuthenticationProvider()
        provider.setServiceProperties(serviceProperties())
        provider.setTicketValidator(ticketValidator())
        provider.setUserDetailsService(CustomUserDetailsService())
        provider.setKey("CAS_PROVIDER_LOCALHOST_9000")
        return provider
    }
}
