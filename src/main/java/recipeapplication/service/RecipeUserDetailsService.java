package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;
import recipeapplication.security.RecipeUserDetails;

import java.util.Optional;

@Service
public class RecipeUserDetailsService implements UserDetailsService
{
    private UserRepository userRepository;

    @Autowired
    public RecipeUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username)
    {
        Optional<User> result = userRepository.findByUsername(username);

        if (!result.isPresent())
        {
            throw new UsernameNotFoundException(username);
        }

        return new RecipeUserDetails(result.get());
    }
}
