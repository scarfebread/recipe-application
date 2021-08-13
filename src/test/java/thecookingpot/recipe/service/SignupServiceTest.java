package thecookingpot.recipe.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import thecookingpot.recipe.dto.UserDto;
import thecookingpot.recipe.exception.EmailExistsException;
import thecookingpot.recipe.exception.UsernameExistsException;
import thecookingpot.recipe.model.User;
import thecookingpot.recipe.repository.UserRepository;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignupServiceTest
{
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";
    private static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";
    private static final String EMAIL = "email@email.com"
;
    private UserRepository userRepository;
    private SignupService signupService;

    @Before
    public void setup()
    {
        userRepository = mock(UserRepository.class);

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        signupService = new SignupService(userRepository, passwordEncoder);
    }

    @Test(expected = UsernameExistsException.class)
    public void shouldThrowUsernameExistsExceptionWhenUsernameAlreadyExists() throws Exception
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER);
        userDto.setEmail(EMAIL);

        when(userRepository.findByUsername(USER)).thenReturn(Optional.of(new User()));

        signupService.registerNewUser(userDto);
    }

    @Test(expected = EmailExistsException.class)
    public void shouldThrowEmailExistsExceptionWhenUsernameAlreadyExists() throws Exception
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER);
        userDto.setEmail(EMAIL);

        when(userRepository.findByEmail(USER)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new User()));

        signupService.registerNewUser(userDto);
    }

    @Test
    public void shouldSaveValidUserDtoInDatabase() throws Exception
    {
        UserDto userDto = new UserDto();

        userDto.setUsername(USER);
        userDto.setPassword(PASSWORD);
        userDto.setEmail(EMAIL);

        when(userRepository.findByUsername(USER)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(EMAIL)).thenReturn(Optional.empty());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        signupService.registerNewUser(userDto);

        verify(userRepository).save(argumentCaptor.capture());

        assertEquals(USER, argumentCaptor.getValue().getUsername());
        assertEquals(EMAIL, argumentCaptor.getValue().getEmail());
        assertEquals(ENCODED_PASSWORD, argumentCaptor.getValue().getPassword());
        assertTrue(argumentCaptor.getValue().getNewUser());
    }
}
