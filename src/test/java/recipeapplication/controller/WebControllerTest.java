package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.service.UserService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class WebControllerTest
{
    private static final String VALID_TOKEN = "VALID_TOKEN";
    private static final String INVALID_TOKEN = "INVALID_TOKEN";

    private WebController controller;

    @Before
    public void setup() throws InvalidPasswordTokenException
    {
        UserService userService = mock(UserService.class);

        doThrow(InvalidPasswordTokenException.class).when(userService).processPasswordResetToken(INVALID_TOKEN);

        controller = new WebController(userService, null);
    }

    @Test
    public void shouldGetChangePasswordTemplateForValidChangePasswordToken()
    {
        assert controller.changePassword(VALID_TOKEN).equals("change_password.html");
    }

    @Test
    public void shouldGetErrorTemplateForInvalidChangePasswordToken()
    {
        assert controller.changePassword(INVALID_TOKEN).equals("invalid_token.html");
    }
}
