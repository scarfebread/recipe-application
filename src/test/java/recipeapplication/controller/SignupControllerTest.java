package recipeapplication.controller;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.service.SignupService;

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

        ResponseEntity response = new SignupController(null).signup(null, errors);

        assert response.getStatusCode().value() == 400;
        assert response.getBody() == "Invalid user information supplied";
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

        assert response.getStatusCode().value() == 400;
        assert response.getBody() == "Username already exists";
    }

    @Test
    public void shouldReturn201WhenUserCreatedSuccessfully() throws Exception
    {
        UserDto userDto = new UserDto();
        Errors errors = mock(Errors.class);
        SignupService signupService = mock(SignupService.class);

        when(errors.hasErrors()).thenReturn(false);

        ResponseEntity response = new SignupController(signupService).signup(userDto, errors);

        assert response.getStatusCode().value() == 201;
    }
}
