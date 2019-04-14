package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignupServiceTest
{
    private static final String USER1 = "USER1";
    private static final String USER2 = "USER2";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "email@email.com"
;
    private UserRepository userRepository;

    @Before
    public void setup()
    {
        userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername(USER1)).thenReturn(new User());
        when(userRepository.findByUsername(USER2)).thenReturn(null);
    }

    @Test(expected = UsernameExistsException.class)
    public void shouldThrowUsernameExistsExceptionWhenUsernameAlreadyExists() throws UsernameExistsException
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER1);

        new SignupService(userRepository).registerNewUser(userDto);
    }

    @Test
    public void shouldSaveValidUserDtoInDatabase() throws UsernameExistsException
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER2);
        userDto.setPassword(PASSWORD);
        userDto.setEmail(EMAIL);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        new SignupService(userRepository).registerNewUser(userDto);

        verify(userRepository).save(argumentCaptor.capture());

        assert argumentCaptor.getValue().getUsername().equals(USER2);
        assert argumentCaptor.getValue().getEmail().equals(EMAIL);

        // Although we're not testing the encrypted password, we can test that it's not null and different to the
        // unencrypted password
        assert argumentCaptor.getValue().getPassword() != null;
        assert !argumentCaptor.getValue().getPassword().equals(PASSWORD);
    }
}
