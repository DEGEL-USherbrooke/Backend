package ca.usherbrooke.degel.config

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
    }
}