package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import recipeapplication.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerTest
{
    private UserService userService;
    private UserController userController;

    @Before
    public void setup()
    {
        userService = mock(UserService.class);

        userController = new UserController(userService);
    }

    @Test
    public void shouldDeleteUserSuccessfully()
    {
        ResponseEntity responseEntity = userController.deleteUser();

        verify(userService).deleteAccount();

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Account deleted", responseEntity.getBody());
    }
}
