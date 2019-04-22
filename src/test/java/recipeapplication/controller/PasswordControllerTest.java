package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.PasswordDto;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.model.User;
import recipeapplication.service.AuthService;
import recipeapplication.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

public class PasswordControllerTest
{
    private static final String SERVER_NAME = "SERVER_NAME";
    private static final int SERVER_PORT = 8080;
    private static final String PASSWORD = "PASSWORD";

    private HttpServletRequest request;
    private AuthService authService;
    private UserService userService;
    private Errors errors;

    @Before
    public void setup()
    {
        authService = mock(AuthService.class);
        request = mock(HttpServletRequest.class);
        userService = mock(UserService.class);
        errors = mock(Errors.class);

        when(request.getServerPort()).thenReturn(SERVER_PORT);
        when(request.getServerName()).thenReturn(SERVER_NAME);
    }

    @Test
    public void shouldReturnCreatedStatusWhenUserNotFoundExceptionIsThrown() throws UserNotFoundException
    {
        doThrow(UserNotFoundException.class).when(userService).createPasswordResetToken(any(), any());

        PasswordController passwordController = new PasswordController(userService, authService);

        ResponseEntity response =  passwordController.createPasswordReset(request, null);

        assert response.getStatusCodeValue() == 201;
        assert response.getBody().equals("Created");
    }

    @Test
    public void shouldReturnCreatedStatusWhenPasswordResetIsCreatedSuccessfully() throws UserNotFoundException
    {
        PasswordController passwordController = new PasswordController(userService, authService);

        UserDto userDto = new UserDto();

        ResponseEntity response =  passwordController.createPasswordReset(request, userDto);

        assert response.getStatusCodeValue() == 201;
        assert response.getBody().equals("Created");

        verify(userService).createPasswordResetToken(String.format("%s:%s", SERVER_NAME, SERVER_PORT), userDto);
    }

    @Test
    public void shouldGet400ResponseChangingPasssordWhenNoUserIsLoggedIn()
    {
        when(authService.getLoggedInUser()).thenReturn(null);

        PasswordController passwordController = new PasswordController(userService, authService);

        ResponseEntity response = passwordController.changePassword(new PasswordDto(), errors);

        assert response.getStatusCodeValue() == 400;
        assert response.getBody().equals("Invalid session");
    }

    @Test
    public void shouldGet400ResponseChangingPasssordWithInvalidPasswordDto()
    {
        when(authService.getLoggedInUser()).thenReturn(new User());
        when(errors.hasErrors()).thenReturn(true);

        PasswordController passwordController = new PasswordController(userService, authService);

        ResponseEntity response = passwordController.changePassword(new PasswordDto(), errors);

        assert response.getStatusCodeValue() == 400;
        assert response.getBody().equals("Invalid password");
    }

    @Test
    public void shouldGet201ResponseChangingPasssordSuccessfully()
    {
        User user = new User();
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword(PASSWORD);

        when(authService.getLoggedInUser()).thenReturn(user);
        when(errors.hasErrors()).thenReturn(false);

        PasswordController passwordController = new PasswordController(userService, authService);

        ResponseEntity response = passwordController.changePassword(passwordDto, errors);

        assert response.getStatusCodeValue() == 201;
        assert response.getBody().equals("Created");

        verify(userService).changePassword(user, PASSWORD);
    }
}
