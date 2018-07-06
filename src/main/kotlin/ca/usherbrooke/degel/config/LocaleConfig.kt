package ca.usherbrooke.degel.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class LocaleConfig {
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("locale/messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}