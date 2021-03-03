package thecookingpot.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import thecookingpot.security.Role
import thecookingpot.recipe.service.RecipeUserDetailsService

@Configuration
@EnableWebSecurity
open class WebSecurityConfig @Autowired constructor(private val userDetailsService: RecipeUserDetailsService) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/password-reset", "/api/signup", "/signup", "/reset-password", "/change-password-with-token", "/api/signup", "/css/**", "/images/**", "/js/**", "/react/**")
                .permitAll()
                .antMatchers("/", "/recipe", "/shopping-list", "/inventory", "/delete-account", "/change-password").hasAuthority(Role.USER.toString())
                .antMatchers("/api/change-password").hasAnyAuthority(Role.CHANGE_PASSWORD.toString(), Role.USER.toString())
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login")
                .and()
            .logout().logoutSuccessUrl("/login").deleteCookies("JSESSIONID").invalidateHttpSession(true)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    @Bean
    open fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }

}