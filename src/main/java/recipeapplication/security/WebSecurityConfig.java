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
        http
            .authorizeRequests()
                .antMatchers("/signup", "/api/signup", "/css/**", "/images/**", "/js/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .antMatchers("/").hasRole("USER")
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout();
    }

    @Override
    public void configure(WebSecurity web)
    {
        // TODO this shouldn't be required
        web.ignoring().antMatchers("/api/signup");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
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
