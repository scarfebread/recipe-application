package thecookingpot.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import thecookingpot.exception.UserNotFoundException
import thecookingpot.security.AuthenticationPrincipal
import thecookingpot.service.UserService


@Configuration
@EnableWebSecurity
/**
 * TODO
 * - logout
 * - exception handling
 * - redundant code
 * - change password
 */
class WebSecurityConfig @Autowired constructor(private val userService: UserService) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        /**
         * should be http {...}
         */
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(
                "/api/password-reset",
                "/api/signup",
                "/signup",
                "/reset-password",
                "/change-password-with-token",
                "/api/signup",
                "/css/**",
                "/images/**",
                "/js/**",
                "/react/**",
            ).permitAll()
            .antMatchers(
                "/",
                "/recipe",
                "/shopping-list",
                "/inventory",
                "/delete-account",
                "/change-password",
            ).authenticated()
            .and()
            .oauth2Login(withDefaults())
//            .exceptionHandling()
//                .accessDeniedPage("/login")
//                .and()
//            .logout().logoutSuccessUrl("/login").deleteCookies("JSESSIONID").invalidateHttpSession(true)
    }

    @Bean // TODO not required anymore
    open fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest ->
            val oidcUser = delegate.loadUser(userRequest)
            val username = oidcUser.subject

            oidcUser.accessTokenHash

            val user = try {
                userService.getUser(username)
            } catch (e: UserNotFoundException) {
                userService.createUser(username, oidcUser.getClaim("email"))
            }

            AuthenticationPrincipal(user, oidcUser)
        }
    }
}