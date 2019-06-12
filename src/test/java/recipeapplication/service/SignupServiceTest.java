package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignupServiceTest
{
    private static final String USER1 = "USER1";
    private static final String USER2 = "USER2";
    private static final String PASSWORD = "PASSWORD";
    private static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";
    private static final String EMAIL = "email@email.com"
;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup()
    {
        userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername(USER1)).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername(USER2)).thenReturn(Optional.empty());

        passwordEncoder = mock(PasswordEncoder.class);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    @Test(expected = UsernameExistsException.class)
    public void shouldThrowUsernameExistsExceptionWhenUsernameAlreadyExists() throws UsernameExistsException
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER1);

        new SignupService(userRepository, new BCryptPasswordEncoder(11)).registerNewUser(userDto);
    }

    @Test
    public void shouldSaveValidUserDtoInDatabase() throws UsernameExistsException
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER2);
        userDto.setPassword(PASSWORD);
        userDto.setEmail(EMAIL);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        new SignupService(userRepository, passwordEncoder).registerNewUser(userDto);

        verify(userRepository).save(argumentCaptor.capture());

        assertEquals(USER2, argumentCaptor.getValue().getUsername());
        assertEquals(EMAIL, argumentCaptor.getValue().getEmail());
        assertEquals(ENCODED_PASSWORD, argumentCaptor.getValue().getPassword());
    }
}
