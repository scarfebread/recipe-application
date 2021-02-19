package thecookingpot.recipe.service;

import org.junit.Before;
import org.junit.Test;
import thecookingpot.recipe.model.User;
import thecookingpot.recipe.repository.IngredientRepository;

import static org.mockito.Mockito.*;

public class IngredientServiceTest
{
    private User user;
    private IngredientRepository ingredientRepository;
    private IngredientService ingredientService;

    @Before
    public void setup()
    {
        user = new User();

        ingredientRepository = mock(IngredientRepository.class);
        AuthService authService = mock(AuthService.class);

        when(authService.getLoggedInUser()).thenReturn(user);

        ingredientService = new IngredientService(ingredientRepository, authService);
    }

    @Test
    public void shouldDeleteAllIngredients()
    {
        ingredientService.deleteAll();

        verify(ingredientRepository).deleteAllByUser(user);
    }
}
