package recipeapplication.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;
import recipeapplication.security.RecipeUserDetails;
import recipeapplication.security.Role;

import java.util.Collections;

@Service
public class AuthService
{
    public User getLoggedInUser()
    {
        return ((RecipeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    public void disablePasswordReset()
    {
        RecipeUserDetails user = ((RecipeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        user.setChangePasswordAccess(false);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null
        ));
    }

    public void authenticateUser(RecipeUserDetails userDetails, Role role)
    {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role.toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public void disableUserSession()
    {
        SecurityContextHolder.clearContext();
    }
}
