package recipeapplication.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;
import recipeapplication.security.RecipeUserDetails;

@Service
public class AuthService
{
    public User getLoggedInUser()
    {
        return ((RecipeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
