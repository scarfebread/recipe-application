package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.model.PasswordResetToken;
import recipeapplication.model.User;
import recipeapplication.repository.PasswordTokenRepository;
import recipeapplication.repository.UserRepository;
import recipeapplication.security.RecipeUserDetails;
import recipeapplication.security.Role;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest
{
    private static final String INVALID_USERNAME = "INVALID_USERNAME";
    private static final String INVALID_EMAIL = "INVALID_EMAIL";
    private static final String VALID_EMAIL = "VALID_EMAIL";
    private static final String VALID_USERNAME = "VALID_USERNAME";
    private static final String SERVER_NAME = "SERVER_NAME";
    private static final String VALID_TOKEN = "VALID_TOKEN";
    private static final String INVALID_TOKEN = "INVALID_TOKEN";
    private static final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    private static final String PASSWORD = "PASSWORD";
    private static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";

    private UserRepository userRepository;
    private PasswordTokenRepository passwordTokenRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;
    private UserService userService;
    private EmailService emailService;

    @Before
    public void setup()
    {
        userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername(INVALID_USERNAME)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(Optional.of(new User()));
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(new User()));

        Calendar oneHourAgo = Calendar.getInstance();
        oneHourAgo.add(Calendar.HOUR_OF_DAY, -1);

        Calendar oneHourInTheFuture = Calendar.getInstance();
        oneHourInTheFuture.add(Calendar.HOUR_OF_DAY, 1);

        User user = new User();
        user.setUsername(VALID_USERNAME);

        PasswordResetToken validPasswordResetToken = new PasswordResetToken();
        validPasswordResetToken.setExpiryDate(oneHourInTheFuture.getTime());
        validPasswordResetToken.setUser(user);

        PasswordResetToken expiredPasswordResetToken = new PasswordResetToken();
        expiredPasswordResetToken.setExpiryDate(oneHourAgo.getTime());

        passwordTokenRepository = mock(PasswordTokenRepository.class);

        when(passwordTokenRepository.findByToken(INVALID_TOKEN)).thenReturn(Optional.empty());
        when(passwordTokenRepository.findByToken(VALID_TOKEN)).thenReturn(Optional.of(validPasswordResetToken));
        when(passwordTokenRepository.findByToken(EXPIRED_TOKEN)).thenReturn(Optional.of(expiredPasswordResetToken));

        passwordEncoder = mock(PasswordEncoder.class);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        authService = mock(AuthService.class);
        emailService = mock(EmailService.class);

        userService = new UserService(userRepository, passwordTokenRepository, emailService, passwordEncoder, authService);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenNoUsernameOrEmailInRequest() throws UserNotFoundException
    {
        userService.createPasswordResetToken("", new UserDto());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUsernameDoesNotExist() throws UserNotFoundException
    {
        UserDto userDto = new UserDto();
        userDto.setUsername(INVALID_USERNAME);

        userService.createPasswordResetToken(SERVER_NAME, userDto);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenEmailDoesNotExist() throws UserNotFoundException
    {
        UserDto userDto = new UserDto();
        userDto.setEmail(INVALID_EMAIL);

        userService.createPasswordResetToken(SERVER_NAME, userDto);
    }

    @Test
    public void shouldCreatePasswordResetTokenForValidUsername() throws UserNotFoundException
    {
        UserDto userDto = new UserDto();
        userDto.setUsername(VALID_USERNAME);

        ArgumentCaptor<PasswordResetToken> argumentCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);

        userService.createPasswordResetToken(SERVER_NAME, userDto);

        verify(emailService).sendPasswordReset(any(User.class), anyString(), eq(SERVER_NAME));
        verify(passwordTokenRepository).save(argumentCaptor.capture());

        assertNotNull(argumentCaptor.getValue().getUser());
        assertNotNull(argumentCaptor.getValue().getToken());
    }

    @Test
    public void shouldCreatePasswordResetTokenForValidEmail() throws UserNotFoundException
    {
        UserDto userDto = new UserDto();
        userDto.setEmail(VALID_EMAIL);

        ArgumentCaptor<PasswordResetToken> argumentCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);

        userService.createPasswordResetToken(SERVER_NAME, userDto);

        verify(emailService).sendPasswordReset(any(User.class), anyString(), eq(SERVER_NAME));
        verify(passwordTokenRepository).save(argumentCaptor.capture());

        assertNotNull(argumentCaptor.getValue().getUser());
        assertNotNull(argumentCaptor.getValue().getToken());
    }

    @Test(expected = InvalidPasswordTokenException.class)
    public void shouldThrowInvalidPasswordTokenExceptionWhenTokenDoesNotExist() throws InvalidPasswordTokenException
    {
        userService.processPasswordResetToken(INVALID_TOKEN);
    }

    @Test(expected = InvalidPasswordTokenException.class)
    public void shouldThrowInvalidPasswordTokenExceptionWhenTokenIsExpired() throws InvalidPasswordTokenException
    {
        userService.processPasswordResetToken(EXPIRED_TOKEN);
    }

    @Test
    public void shouldEnablePasswordResetForValidToken() throws InvalidPasswordTokenException
    {
        ArgumentCaptor<RecipeUserDetails> argumentCaptor = ArgumentCaptor.forClass(RecipeUserDetails.class);

        userService.processPasswordResetToken(VALID_TOKEN);

        verify(authService).authenticateUser(argumentCaptor.capture(), eq(Role.CHANGE_PASSWORD));

        RecipeUserDetails recipeUserDetails = argumentCaptor.getValue();

        assertEquals(VALID_USERNAME, recipeUserDetails.getUser().getUsername());
        assertTrue(recipeUserDetails.isChangePasswordAccess());
    }

    @Test
    public void shouldChangePasswordSuccessfully()
    {
        User user = new User();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        userService.changePassword(user, PASSWORD);

        verify(userRepository).save(argumentCaptor.capture());
        verify(passwordTokenRepository).deleteByUser(user);
        verify(authService).disablePasswordReset();

        assertEquals(ENCODED_PASSWORD, argumentCaptor.getValue().getPassword());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() throws UserNotFoundException
    {
        when(userRepository.findByUsername(INVALID_USERNAME)).thenReturn(Optional.empty());

        userService.getUser(INVALID_USERNAME);
    }

    @Test
    public void shouldGetUserWhenValidUsernameProvided() throws UserNotFoundException
    {
        User user = new User();

        when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUser(VALID_USERNAME));
    }

    @Test
    public void shouldDeleteAccountSuccessfully()
    {
        User user = new User();

        when(authService.getLoggedInUser()).thenReturn(user);

        userService.deleteAccount();

        verify(userRepository).delete(user);
    }

    @Test
    public void shouldTurnOffInstructionsIfTheUserIsNew()
    {
        User user = new User();
        user.setNewUser(true);

        when(authService.getLoggedInUser()).thenReturn(user);

        userService.turnOffInstructions();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(argumentCaptor.capture());

        assertFalse(argumentCaptor.getValue().isNewUser());
    }

    @Test
    public void shouldNotTurnOffInstructionsIfTheUserIsNotNew()
    {
        User user = new User();
        user.setNewUser(false);

        when(authService.getLoggedInUser()).thenReturn(user);

        userService.turnOffInstructions();

        verify(userRepository, never()).save(any());
    }
}