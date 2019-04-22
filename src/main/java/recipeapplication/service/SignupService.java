package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;

@Service
public class SignupService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(UserDto userDto) throws UsernameExistsException
    {
        if (usernameAlreadyRegistered(userDto.getUsername()))
        {
            throw new UsernameExistsException();
        }

        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        userRepository.save(user);
    }

    private boolean usernameAlreadyRegistered(String username)
    {
        return (userRepository.findByUsername(username).isPresent());
    }
}
