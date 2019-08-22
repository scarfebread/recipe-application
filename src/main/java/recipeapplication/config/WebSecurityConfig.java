package recipeapplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipeapplication.security.Role;
import recipeapplication.service.RecipeUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private RecipeUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(RecipeUserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/password-reset", "/api/signup", "/signup", "/reset-password", "/change-password-with-token", "/api/signup", "/css/**", "/images/**", "/js/**")
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
            .logout().logoutSuccessUrl("/login").deleteCookies("JSESSIONID").invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder(11);
    }
}
