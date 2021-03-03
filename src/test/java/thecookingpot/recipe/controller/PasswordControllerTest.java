package thecookingpot.recipe.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import thecookingpot.recipe.dto.PasswordDto;
import thecookingpot.recipe.dto.PasswordResetDto;
import thecookingpot.recipe.exception.UserNotFoundException;
import thecookingpot.recipe.model.User;
import thecookingpot.security.service.AuthService;
import thecookingpot.recipe.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.TestCase.assertEquals;
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

        ResponseEntity response =  passwordController.createPasswordReset(request, new PasswordResetDto());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Created", response.getBody());
    }

    @Test
    public void shouldReturnCreatedStatusWhenPasswordResetIsCreatedSuccessfully() throws UserNotFoundException
    {
        PasswordController passwordController = new PasswordController(userService, authService);

        PasswordResetDto passwordResetDto = new PasswordResetDto();

        ResponseEntity response =  passwordController.createPasswordReset(request, passwordResetDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Created", response.getBody());

        verify(userService).createPasswordResetToken(String.format("%s:%s", SERVER_NAME, SERVER_PORT), passwordResetDto);
    }

    @Test
    public void shouldGet400ResponseChangingPasssordWithInvalidPasswordDto()
    {
        when(authService.getLoggedInUser()).thenReturn(new User());
        when(errors.hasErrors()).thenReturn(true);

        PasswordController passwordController = new PasswordController(userService, authService);

        ResponseEntity response = passwordController.changePassword(new PasswordDto(), errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid password", response.getBody());
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

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Created", response.getBody());

        verify(userService).changePassword(user, PASSWORD);
    }
}
