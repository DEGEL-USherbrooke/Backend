package ca.usherbrooke.degel.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.cas.web.CasAuthenticationFilter
import org.springframework.security.cas.ServiceProperties
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.cas.authentication.CasAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.web.AuthenticationEntryPoint
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig(val authenticationProvider: CasAuthenticationProvider,
                     val authenticationEntryPoint: AuthenticationEntryPoint) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .regexMatchers("/secured.*", "/login")
                .authenticated()
                .and()
                .authorizeRequests()
                .regexMatchers("/")
                .permitAll()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(authenticationProvider)
    }

    @Throws(Exception::class)
    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(
                Arrays.asList(authenticationProvider as AuthenticationProvider))
    }

    @Bean
    @Throws(Exception::class)
    fun casAuthenticationFilter(sP: ServiceProperties): CasAuthenticationFilter {
        val filter = CasAuthenticationFilter()
        filter.setServiceProperties(sP)
        filter.setAuthenticationManager(authenticationManager())
        return filter
    }

}