package thecookingpot.recipe.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit

@Configuration
@EnableWebMvc
open class MvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/react/**")
                .addResourceLocations("classpath:/static/react/")
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
    }
}
