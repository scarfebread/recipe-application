package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.model.Recipe;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecentlyViewedRepository;
import recipeapplication.repository.RecipeRepository;
import recipeapplication.repository.StepRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceTest
{
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private StepRepository stepRepository;
    private AuthService authService;
    private RecentlyViewedRepository recentlyViewedRepository;
    private RecipeService recipeService;
    private User loggedInUser;

    @Before
    public void setup()
    {
        recipeRepository = mock(RecipeRepository.class);
        ingredientRepository = mock(IngredientRepository.class);
        stepRepository = mock(StepRepository.class);
        authService = mock(AuthService.class);
        recentlyViewedRepository = mock(RecentlyViewedRepository.class);

        loggedInUser = new User();
        loggedInUser.setUsername("testuser");
        loggedInUser.setId(1L);

        when(authService.getLoggedInUser()).thenReturn(loggedInUser);

        recipeService = new RecipeService(recipeRepository, ingredientRepository, stepRepository, authService, recentlyViewedRepository);
    }

    @Test
    public void shouldCreateRecipeSuccessfully()
    {
        CreateRecipeDto recipeDto = new CreateRecipeDto();

        recipeDto.setTitle("Test recipe");

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        recipeService.createRecipe(recipeDto);

        verify(recipeRepository).save(argumentCaptor.capture());

        Recipe recipe = argumentCaptor.getValue();

        assertEquals(recipeDto.getTitle(), recipe.getTitle());
        assertEquals(loggedInUser.getId(), recipe.getUserId());
        assertEquals(new Long(0L), recipe.getRating());
        assertEquals(new Long(1L), recipe.getServes());
        assertEquals("Medium", recipe.getDifficulty());
        assertEquals("00:00", recipe.getTotalTime());
    }
}
