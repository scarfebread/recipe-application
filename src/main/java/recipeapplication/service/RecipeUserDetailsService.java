package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;
import recipeapplication.security.RecipeUserDetails;

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
        User user = userRepository.findByUsername(username);

        if (user == null)
        {
            throw new UsernameNotFoundException(username);
        }

        return new RecipeUserDetails(user);
    }
}
