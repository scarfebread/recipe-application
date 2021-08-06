package thecookingpot.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import thecookingpot.model.User;
import thecookingpot.repository.UserRepository;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeUserDetailsServiceTest
{
    private static final String VALID_USER = "VALID_USER";
    private static final String INVALID_USER = "INVALID_USER";

    private RecipeUserDetailsService service;

    @Before
    public void setup()
    {
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findByUsername(VALID_USER)).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername(INVALID_USER)).thenReturn(Optional.empty());

        service = new RecipeUserDetailsService(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist()
    {
        service.loadUserByUsername(INVALID_USER);
    }

    @Test
    public void shouldGetUserForValidUsername()
    {
        assertNotNull(service.loadUserByUsername(VALID_USER));
    }
}
