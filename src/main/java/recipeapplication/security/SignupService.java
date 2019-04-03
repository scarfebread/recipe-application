package recipeapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.model.User;
import recipeapplication.repository.UserRepository;

@Service
public class SignupService
{
    private UserRepository userRepository;

    @Autowired
    public SignupService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
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
                new BCryptPasswordEncoder(11).encode(userDto.getPassword())
        );

        userRepository.save(user);
    }

    private boolean usernameAlreadyRegistered(String username)
    {
        return (userRepository.findByUsername(username) != null);
    }
}
