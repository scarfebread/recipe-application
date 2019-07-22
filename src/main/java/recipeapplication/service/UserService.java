package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.model.PasswordResetToken;
import recipeapplication.model.User;
import recipeapplication.repository.PasswordTokenRepository;
import recipeapplication.repository.UserRepository;
import recipeapplication.security.RecipeUserDetails;
import recipeapplication.security.Role;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService
{
    private UserRepository userRepository;
    private PasswordTokenRepository passwordTokenRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordTokenRepository passwordTokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            AuthService authService)
    {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public void createPasswordResetToken(String serverName, UserDto userDto) throws UserNotFoundException
    {
        Optional<User> result;

        if (userDto.getUsername() != null)
        {
            result = userRepository.findByUsername(userDto.getUsername());
        }
        else if (userDto.getEmail() != null)
        {
            result = userRepository.findByEmail(userDto.getEmail());
        }
        else
        {
            throw new UserNotFoundException();
        }

        if (!result.isPresent())
        {
            throw new UserNotFoundException();
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, result.get());

        passwordTokenRepository.save(passwordResetToken);

        emailService.sendPasswordReset(result.get(), token, serverName);
    }

    public void processPasswordResetToken(String token) throws InvalidPasswordTokenException
    {
        Optional<PasswordResetToken> passwordResetToken = passwordTokenRepository.findByToken(token);

        if (!passwordResetToken.isPresent() || passwordResetToken.get().isExpired())
        {
            throw new InvalidPasswordTokenException();
        }

        User user = passwordResetToken.get().getUser();
        RecipeUserDetails recipeUserDetails = new RecipeUserDetails(user);
        recipeUserDetails.setChangePasswordAccess(true);

        authService.authenticateUser(recipeUserDetails, Role.CHANGE_PASSWORD);
    }

    public void changePassword(User user, String password)
    {
        user.setPassword(
                passwordEncoder.encode(password)
        );

        userRepository.save(user);
        passwordTokenRepository.deleteByUser(user);

        authService.disablePasswordReset();
    }

    public User getUser(String username) throws UserNotFoundException
    {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent())
        {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    public void deleteAccount()
    {
        User user = authService.getLoggedInUser();

        userRepository.delete(user);
    }

    public void turnOffInstructions()
    {
        User user = authService.getLoggedInUser();

        if (user.isNewUser())
        {
            user.setNewUser(false);
            userRepository.save(user);
        }
    }
}
