package recipeapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        // TODO auto login failing after change password

        // TODO enable the CSRF token
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/signup", "/resetPassword", "/changePassword", "/api/signup", "/css/**", "/images/**", "/js/**")
                .permitAll()
                .antMatchers("/", "/recipe", "/deleteAccount").hasAuthority(Role.USER.toString())
                .antMatchers("/api/change_password").hasAuthority(Role.CHANGE_PASSWORD.toString())
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
    public void configure(WebSecurity web)
    {
        // TODO there might be a better way to do this
        web.ignoring().antMatchers("/api/signup", "/api/password_reset");
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
