package recipeapplication.service;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import recipeapplication.model.User;
import recipeapplication.security.RecipeUserDetails;

public class AuthServiceTest
{
    @Test
    public void shouldGetLoggedInUserSuccessfully()
    {
        User user = new User();

        Authentication auth = new UsernamePasswordAuthenticationToken(new RecipeUserDetails(user), null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assert user == new AuthService().getLoggedInUser();
    }
}
