package recipeapplication.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.model.RecentlyViewed;
import recipeapplication.model.User;
import recipeapplication.service.AuthService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebControllerTest
{
    private static final String VALID_TOKEN = "VALID_TOKEN";
    private static final String INVALID_TOKEN = "INVALID_TOKEN";
    private static final String USERNAME = "USERNAME";

    private WebController controller;
    private List<RecentlyViewed> recentlyViewed;

    @Before
    public void setup() throws InvalidPasswordTokenException
    {
        UserService userService = mock(UserService.class);
        RecipeService recipeService = mock(RecipeService.class);
        AuthService authService = mock(AuthService.class);

        User user = new User();
        user.setUsername(USERNAME);

        recentlyViewed = new ArrayList<>();
        recentlyViewed.add(new RecentlyViewed());
        recentlyViewed.add(new RecentlyViewed());

        doThrow(InvalidPasswordTokenException.class).when(userService).processPasswordResetToken(INVALID_TOKEN);
        when(recipeService.getRecentlyViewed()).thenReturn(recentlyViewed);
        when(authService.getLoggedInUser()).thenReturn(user);

        controller = new WebController(userService, recipeService, authService);
    }

    @Test
    public void shouldGetHomeWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.home(model);

        assertEquals("home.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldGetChangePasswordTemplateForValidChangePasswordToken()
    {
        assertEquals("change_password.html", controller.changePassword(VALID_TOKEN));
    }

    @Test
    public void shouldGetErrorTemplateForInvalidChangePasswordToken()
    {
        assertEquals("invalid_token.html", controller.changePassword(INVALID_TOKEN));
    }
}
