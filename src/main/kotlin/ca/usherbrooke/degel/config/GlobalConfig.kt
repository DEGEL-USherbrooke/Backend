package ca.usherbrooke.degel.config

import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.apache.http.impl.client.LaxRedirectStrategy
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory



@Configuration
class GlobalConfig {
    @Bean
    fun restTemplate() : RestTemplate {
        val restTemplate = RestTemplate()
        val factory = HttpComponentsClientHttpRequestFactory()
        val httpClient = HttpClientBuilder
                .create()
                .setRedirectStrategy(LaxRedirectStrategy())
                .build()
        factory.httpClient = httpClient
        restTemplate.requestFactory = factory
        return restTemplate
    }
}