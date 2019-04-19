package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService
{
    private UserRepository userRepository;
    private PasswordTokenRepository passwordTokenRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordTokenRepository passwordTokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPasswordResetToken(String serverName, UserDto userDto) throws UserNotFoundException
    {
        User user;

        if (userDto.getUsername() != null)
        {
            user = userRepository.findByUsername(userDto.getUsername());
        }
        else if (userDto.getEmail() != null)
        {
            user = userRepository.findByEmail(userDto.getEmail());
        }
        else
        {
            throw new UserNotFoundException();
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);

        passwordTokenRepository.save(passwordResetToken);

        emailService.sendPasswordReset(user, token, serverName);
    }

    public void processPasswordResetToken(String token) throws InvalidPasswordTokenException
    {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);

        if (passwordResetToken == null || passwordResetToken.isExpired())
        {
            throw new InvalidPasswordTokenException();
        }

        User user = passwordResetToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new RecipeUserDetails(user),
                null,
                Collections.singletonList(new SimpleGrantedAuthority("PASSWORD_RESET"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public void changePassword(User user, String password)
    {
        user.setPassword(
                passwordEncoder.encode(password)
        );

        userRepository.save(user);
        passwordTokenRepository.deleteByUser(user);
    }
}
