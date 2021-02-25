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
import thecookingpot.security.RecipeUserDetails;
import thecookingpot.security.Role;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
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
    private AuthService authService;

    @Before
    public void setup()
    {
        userRepository = mock(UserRepository.class);
        authService = mock(AuthService.class);

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        signupService = new SignupService(userRepository, passwordEncoder, authService);
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

        ArgumentCaptor<User> repositoryCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<RecipeUserDetails> authServiceCaptor = ArgumentCaptor.forClass(RecipeUserDetails.class);

        signupService.registerNewUser(userDto);

        verify(userRepository).save(repositoryCaptor.capture());
        verify(authService).authenticateUser(authServiceCaptor.capture(), eq(Role.USER));

        assertEquals(USER, repositoryCaptor.getValue().getUsername());
        assertEquals(EMAIL, repositoryCaptor.getValue().getEmail());
        assertEquals(ENCODED_PASSWORD, repositoryCaptor.getValue().getPassword());
        assertEquals(repositoryCaptor.getValue(), authServiceCaptor.getValue().getUser());
        assertTrue(repositoryCaptor.getValue().getNewUser());
    }
}
