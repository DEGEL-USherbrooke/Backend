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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import java.util.*
import org.jasig.cas.client.session.SingleSignOutFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.authentication.logout.LogoutFilter




@Configuration
@EnableWebSecurity
class SecurityConfig(val authenticationProvider: CasAuthenticationProvider,
                     val serviceProperties: ServiceProperties,
                     val authenticationEntryPoint: AuthenticationEntryPoint,
                     @Value("\${app.location}") val location: String,
                     @Value("\${app.security.cas-server}") val casServer: String) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/login**", "/logout**", "/oauth/authorize", "/oauth/callback")
                .authenticated()
                .and()
                .addFilter(casAuthenticationFilter())
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter::class.java)
                .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter::class.java)
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)

        http.logout().logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
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

    @Bean
    fun singleSignOutFilter(): SingleSignOutFilter {
        val singleSignOutFilter = SingleSignOutFilter()
        singleSignOutFilter.setCasServerUrlPrefix(casServer)
        singleSignOutFilter.setIgnoreInitConfiguration(true)
        return singleSignOutFilter
    }

    @Bean
    fun requestCasGlobalLogoutFilter(): LogoutFilter {
        val logoutFilter = LogoutFilter("$casServer/logout?service=$location/logout/success", SecurityContextLogoutHandler())
        logoutFilter.setLogoutRequestMatcher(AntPathRequestMatcher("/logout"))
        return logoutFilter
    }
}