package thecookingpot.recipe.controller;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import thecookingpot.recipe.dto.UserDto;
import thecookingpot.recipe.exception.EmailExistsException;
import thecookingpot.recipe.exception.UsernameExistsException;
import thecookingpot.recipe.service.SignupService;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignupControllerTest
{
    @Test
    public void shouldReturn400WhenRequestHasErrors()
    {
        Errors errors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        SignupService signupService = mock(SignupService.class);

        ResponseEntity response = new SignupController(signupService).signup(new UserDto(), errors);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid user information supplied", response.getBody());
    }

    @Test
    public void shouldReturn400WhenUsernameAlreadyExists() throws Exception
    {
        UserDto userDto = new UserDto();
        Errors errors = mock(Errors.class);
        SignupService signupService = mock(SignupService.class);

        when(errors.hasErrors()).thenReturn(false);
        doThrow(new UsernameExistsException()).when(signupService).registerNewUser(userDto);

        ResponseEntity response = new SignupController(signupService).signup(userDto, errors);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Username already exists", response.getBody());
    }

    @Test
    public void shouldReturn400WhenEmailAlreadyExists() throws Exception
    {
        UserDto userDto = new UserDto();
        Errors errors = mock(Errors.class);
        SignupService signupService = mock(SignupService.class);

        when(errors.hasErrors()).thenReturn(false);
        doThrow(new EmailExistsException()).when(signupService).registerNewUser(userDto);

        ResponseEntity response = new SignupController(signupService).signup(userDto, errors);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Email address already exists", response.getBody());
    }

    @Test
    public void shouldReturn201WhenUserCreatedSuccessfully() throws Exception
    {
        UserDto userDto = new UserDto();
        Errors errors = mock(Errors.class);
        SignupService signupService = mock(SignupService.class);

        when(errors.hasErrors()).thenReturn(false);

        ResponseEntity response = new SignupController(signupService).signup(userDto, errors);

        assertEquals(201, response.getStatusCode().value());
    }
}
