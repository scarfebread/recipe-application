package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import recipeapplication.service.AuthService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerTest
{
    private UserService userService;
    private RecipeService recipeService;
    private AuthService authService;
    private UserController userController;

    @Before
    public void setup()
    {
        userService = mock(UserService.class);
        recipeService = mock(RecipeService.class);
        authService = mock(AuthService.class);

        userController = new UserController(userService, recipeService, authService);
    }

    @Test
    public void shouldDeleteUserSuccessfully()
    {
        ResponseEntity responseEntity = userController.deleteUser();

        verify(userService).deleteAccount();
        verify(authService).disableUserSession();
        verify(recipeService).deleteAllRecipes();

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Account deleted", responseEntity.getBody());
    }
}
