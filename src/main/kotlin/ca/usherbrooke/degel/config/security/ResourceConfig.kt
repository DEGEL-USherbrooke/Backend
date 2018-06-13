package ca.usherbrooke.degel.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.http.HttpMethod
import org.springframework.security.cas.ServiceProperties
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest

@Configuration
@EnableResourceServer
class ResourceConfig(val serviceProperties: ServiceProperties) : ResourceServerConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .requestMatcher(OAuthRequestedMatcher())
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/**").authenticated()
    }

    private class OAuthRequestedMatcher : RequestMatcher {
        override fun matches(request: HttpServletRequest): Boolean {
            // Determine if the resource called is "/api/**"
            var path = request.getServletPath()
            if (path.length >= 5) {
                path = path.substring(0, 5)
                return path == "/api/"
            } else
                return false
        }
    }
}