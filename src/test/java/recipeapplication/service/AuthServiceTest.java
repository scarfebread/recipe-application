package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import recipeapplication.model.User;
import recipeapplication.security.RecipeUserDetails;
import recipeapplication.security.Role;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class AuthServiceTest
{
    private AuthService authService;

    @Before
    public void setup()
    {
        authService = new AuthService();
    }

    @Test
    public void shouldGetLoggedInUserSuccessfully()
    {
        User user = new User();

        authService.authenticateUser(new RecipeUserDetails(user), Role.USER);

        assertEquals(user, authService.getLoggedInUser());
    }

    @Test
    public void shouldDisablePasswordResetPermissionWhenEnabled()
    {
        RecipeUserDetails recipeUserDetails = new RecipeUserDetails(new User());
        recipeUserDetails.setChangePasswordAccess(true);
        authService.authenticateUser(recipeUserDetails, Role.USER);

        authService.disablePasswordReset();

        SecurityContext securityContext = SecurityContextHolder.getContext();

        assertFalse(((RecipeUserDetails) securityContext.getAuthentication().getPrincipal()).isChangePasswordAccess());
        assertEquals(0, securityContext.getAuthentication().getAuthorities().size());
    }

    @Test
    public void shouldNotDisablePasswordResetPermissionWhenNotEnabled()
    {
        RecipeUserDetails recipeUserDetails = new RecipeUserDetails(new User());
        recipeUserDetails.setChangePasswordAccess(false);
        authService.authenticateUser(recipeUserDetails, Role.USER);

        authService.disablePasswordReset();

        SecurityContext securityContext = SecurityContextHolder.getContext();

        assertFalse(((RecipeUserDetails) securityContext.getAuthentication().getPrincipal()).isChangePasswordAccess());
        assertEquals(1, securityContext.getAuthentication().getAuthorities().size());
    }

    @Test
    public void shouldLogInSuccessfully()
    {
        RecipeUserDetails recipeUserDetails = new RecipeUserDetails(new User());
        authService.authenticateUser(recipeUserDetails, Role.USER);

        assertEquals(recipeUserDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    }

    @Test
    public void shouldLogOutSuccessfully()
    {
        RecipeUserDetails recipeUserDetails = new RecipeUserDetails(new User());
        authService.authenticateUser(recipeUserDetails, Role.USER);
        authService.disableUserSession();

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
