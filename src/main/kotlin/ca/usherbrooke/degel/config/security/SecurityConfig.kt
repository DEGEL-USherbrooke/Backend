package ca.usherbrooke.degel.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.cas.ServiceProperties
import org.springframework.security.cas.authentication.CasAuthenticationProvider
import org.springframework.security.cas.web.CasAuthenticationFilter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig(val authenticationProvider: CasAuthenticationProvider,
                     val serviceProperties: ServiceProperties,
                     val authenticationEntryPoint: AuthenticationEntryPoint) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/login**", "/oauth/authorize", "/oauth/callback")
                .authenticated()
                .and()
                .addFilter(casAuthenticationFilter())
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider)
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(
                Arrays.asList(authenticationProvider as AuthenticationProvider))
    }

    @Bean
    fun casAuthenticationFilter(): CasAuthenticationFilter {
        val filter = CasAuthenticationFilter()
        filter.setServiceProperties(serviceProperties)
        filter.setAuthenticationManager(authenticationManager())
        return filter
    }
}