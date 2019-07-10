package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.IngredientDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.*;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecentlyViewedRepository;
import recipeapplication.repository.RecipeRepository;
import recipeapplication.repository.StepRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecipeServiceTest
{
    private static final String NOTES = "NOTES";
    private static final String TITLE = "TITLE";
    private static final Long RATING = 5L;
    private static final Long SERVES = 2L;
    private static final String COOK_TIME = "01:21";
    private static final String PREP_TIME = "02:04";
    private static final String TOTAL_TIME = "03:25";
    private static final String DIFFICULTY = "DIFFICULTY";

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private StepRepository stepRepository;
    private AuthService authService;
    private RecentlyViewedRepository recentlyViewedRepository;
    private EntityManager entityManager;
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
        entityManager = mock(EntityManager.class);

        loggedInUser = new User();
        loggedInUser.setUsername("testuser");
        loggedInUser.setId(1L);

        when(authService.getLoggedInUser()).thenReturn(loggedInUser);

        recipeService = new RecipeService(recipeRepository, ingredientRepository, stepRepository, authService, recentlyViewedRepository, entityManager);
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
        assertEquals(Long.valueOf(0L), recipe.getRating());
        assertEquals(Long.valueOf(1L), recipe.getServes());
        assertEquals("Medium", recipe.getDifficulty());
        assertEquals("00:00", recipe.getTotalTime());
    }

    @Test
    public void shouldGetRecipesSuccessfully()
    {
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

        when(recipeRepository.findByUserId(loggedInUser.getId())).thenReturn(recipes);

        assertEquals(recipes, recipeService.getRecipes());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenRecipeDoesNotExist() throws Exception
    {
        Long recipeId = 2L;

        when(recipeRepository.findByIdAndUserId(recipeId, loggedInUser.getId())).thenReturn(Optional.empty());

        recipeService.getRecipe(recipeId);
    }

    @Test
    public void shouldGetRecipeWhenRecipeExists() throws Exception
    {
        Recipe recipe = new Recipe();
        recipe.setId(3L);

        when(recipeRepository.findByIdAndUserId(recipe.getId(), loggedInUser.getId())).thenReturn(Optional.of(recipe));

        assertEquals(recipe, recipeService.getRecipe(recipe.getId()));
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenDeletingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUserId(recipeDto.getId(), loggedInUser.getId())).thenReturn(Optional.empty());

        recipeService.deleteRecipe(recipeDto);
    }

    @Test
    public void shouldDeleteRecipeSuccessfully() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(3L);

        Recipe recipe = new Recipe();
        recipe.setId(3L);

        when(recipeRepository.findByIdAndUserId(recipe.getId(), loggedInUser.getId())).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(recipeDto);

        verify(recipeRepository).deleteById(recipe.getId());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenUpdatingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUserId(recipeDto.getId(), loggedInUser.getId())).thenReturn(Optional.empty());

        recipeService.updateRecipe(recipeDto);
    }

    @Test
    public void shouldUpdateRecipeSuccessfully() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setNotes(NOTES);
        recipeDto.setCookTime(COOK_TIME);
        recipeDto.setPrepTime(PREP_TIME);
        recipeDto.setDifficulty(DIFFICULTY);
        recipeDto.setRating(RATING);
        recipeDto.setServes(SERVES);

        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setDescription("description");
        ingredientDto1.setQuantity("quantity");

        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setDescription("description");
        ingredientDto2.setQuantity("quantity");

        List<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(ingredientDto1);
        ingredients.add(ingredientDto2);

        List<String> steps = new ArrayList<>();
        steps.add("Step 1");
        steps.add("Step 2");

        recipeDto.setIngredients(ingredients);
        recipeDto.setSteps(steps);

        when(recipeRepository.findByIdAndUserId(recipeDto.getId(), loggedInUser.getId())).thenReturn(Optional.of(new Recipe()));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        recipeService.updateRecipe(recipeDto);

        verify(recipeRepository).save(argumentCaptor.capture());
        verify(ingredientRepository).deleteByRecipe(any(Recipe.class));
        verify(stepRepository).deleteByRecipe(any(Recipe.class));

        Recipe recipe = argumentCaptor.getValue();

        assertEquals(NOTES, recipe.getNotes());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(TOTAL_TIME, recipe.getTotalTime());
        assertEquals(RATING, recipe.getRating());
        assertEquals(SERVES, recipe.getServes());

        assertEquals(ingredients.get(0).getQuantity(), recipe.getIngredients().get(0).getImperial());
        assertEquals(ingredients.get(1).getQuantity(), recipe.getIngredients().get(1).getMetric());

        assertEquals(steps.get(0), recipe.getSteps().get(0).getDescription());
        assertEquals(steps.get(1), recipe.getSteps().get(1).getDescription());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenSharingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUserId(recipeDto.getId(), loggedInUser.getId())).thenReturn(Optional.empty());

        recipeService.shareRecipe(recipeDto, new User());
    }

    @Test
    public void shouldShareRecipeSuccessfully() throws Exception
    {
        User userToShareWith = new User();
        userToShareWith.setId(1L);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setTitle(TITLE);
        recipe.setNotes(NOTES);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setTotalTime(TOTAL_TIME);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setRating(RATING);
        recipe.setServes(SERVES);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(recipe, "Ingredient", "Quantity"));

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(recipe, "Step"));

        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        when(recipeRepository.findByIdAndUserId(recipeDto.getId(), loggedInUser.getId())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        recipeService.shareRecipe(recipeDto, userToShareWith);

        verify(recipeRepository).save(argumentCaptor.capture());
        verify(entityManager).detach(recipe);
        verify(entityManager).detach(ingredients.get(0));
        verify(entityManager).detach(steps.get(0));

        Recipe sharedRecipe = argumentCaptor.getValue();

        assertNull(sharedRecipe.getId());
        assertEquals(TITLE, sharedRecipe.getTitle());
        assertEquals(NOTES, sharedRecipe.getNotes());
        assertEquals(DIFFICULTY, sharedRecipe.getDifficulty());
        assertEquals(COOK_TIME, sharedRecipe.getCookTime());
        assertEquals(PREP_TIME, sharedRecipe.getPrepTime());
        assertEquals(TOTAL_TIME, sharedRecipe.getTotalTime());
        assertEquals(RATING, sharedRecipe.getRating());
        assertEquals(SERVES, sharedRecipe.getServes());
        assertEquals(loggedInUser.getUsername() , sharedRecipe.getSharedBy());
        assertEquals(userToShareWith.getId(), sharedRecipe.getUserId());
        assertEquals(ingredients, sharedRecipe.getIngredients());
        assertEquals(steps, sharedRecipe.getSteps());
    }

    @Test
    public void shouldAddRecentlyViewedByIfNotMostRecentRecipe()
    {
        Recipe mostRecentRecipe = new Recipe();
        mostRecentRecipe.setId(1L);

        RecentlyViewed recentlyViewed1 = new RecentlyViewed();
        recentlyViewed1.setRecipe(mostRecentRecipe);
        recentlyViewed1.setUserId(loggedInUser.getId());

        Recipe aRecentRecipe = new Recipe();
        aRecentRecipe.setId(2L);

        RecentlyViewed recentlyViewed2 = new RecentlyViewed();
        recentlyViewed2.setRecipe(aRecentRecipe);
        recentlyViewed2.setUserId(loggedInUser.getId());

        List<RecentlyViewed> recentlyViewed = new ArrayList<>();
        recentlyViewed.add(recentlyViewed1);
        recentlyViewed.add(recentlyViewed2);

        when(recentlyViewedRepository.findTop5ByUserIdOrderByIdDesc(loggedInUser.getId())).thenReturn(recentlyViewed);

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUserId(loggedInUser.getId());

        ArgumentCaptor<RecentlyViewed> argumentCaptor = ArgumentCaptor.forClass(RecentlyViewed.class);

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository).save(argumentCaptor.capture());

        RecentlyViewed result = argumentCaptor.getValue();

        assertEquals(viewedRecipe, result.getRecipe());
        assertEquals(loggedInUser.getId(), result.getUserId());
    }

    @Test
    public void shouldAddRecentlyViewedByIfNoRecentlyViewedRecipes()
    {
        Recipe mostRecentRecipe = new Recipe();
        mostRecentRecipe.setId(1L);

        when(recentlyViewedRepository.findTop5ByUserIdOrderByIdDesc(loggedInUser.getId())).thenReturn(new ArrayList<>());

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUserId(loggedInUser.getId());

        ArgumentCaptor<RecentlyViewed> argumentCaptor = ArgumentCaptor.forClass(RecentlyViewed.class);

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository).save(argumentCaptor.capture());

        RecentlyViewed result = argumentCaptor.getValue();

        assertEquals(viewedRecipe, result.getRecipe());
        assertEquals(loggedInUser.getId(), result.getUserId());
    }

    @Test
    public void shouldNotAddRecentlyViewedByIfMostRecentRecipe()
    {
        Recipe mostRecentRecipe = new Recipe();
        mostRecentRecipe.setId(2L);

        RecentlyViewed recentlyViewed1 = new RecentlyViewed();
        recentlyViewed1.setRecipe(mostRecentRecipe);
        recentlyViewed1.setUserId(loggedInUser.getId());

        Recipe aRecentRecipe = new Recipe();
        aRecentRecipe.setId(1L);

        RecentlyViewed recentlyViewed2 = new RecentlyViewed();
        recentlyViewed2.setRecipe(aRecentRecipe);
        recentlyViewed2.setUserId(loggedInUser.getId());

        List<RecentlyViewed> recentlyViewed = new ArrayList<>();
        recentlyViewed.add(recentlyViewed1);
        recentlyViewed.add(recentlyViewed2);

        when(recentlyViewedRepository.findTop5ByUserIdOrderByIdDesc(loggedInUser.getId())).thenReturn(recentlyViewed);

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUserId(loggedInUser.getId());

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository, never()).save(any());
    }

    @Test
    public void shouldGetRecentlyViewedSuccessfully()
    {
        List<RecentlyViewed> recentlyViewed = new ArrayList<>();

        recentlyViewed.add(new RecentlyViewed());
        recentlyViewed.add(new RecentlyViewed());

        when(recentlyViewedRepository.findTop5ByUserIdOrderByIdDesc(loggedInUser.getId())).thenReturn(recentlyViewed);

        assertEquals(recentlyViewed, recipeService.getRecentlyViewed());
    }

    @Test
    public void shouldDeleteAllRecipesSuccessfully()
    {
        recipeService.deleteAllRecipes();

        verify(recipeRepository).deleteAllByUserId(loggedInUser.getId());
    }

    @Test
    public void shouldAddIngredientSuccessfully() throws Exception
    {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("Ingredient");
        ingredientDto.setQuantity("Quantity");
        ingredientDto.setRecipe(1L);

        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setRecipe(recipe);
        ingredient.setMetric("metric");
        ingredient.setImperial("imperial");

        when(recipeRepository.findByIdAndUserId(ingredientDto.getRecipe(), loggedInUser.getId())).thenReturn(Optional.of(recipe));
        when(ingredientRepository.save(any())).thenReturn(ingredient);

        Ingredient result = recipeService.addIngredient(ingredientDto);

        verify(ingredientRepository).save(any(Ingredient.class));

        assertEquals(ingredient.getMetric(), result.getMetric());
        assertEquals(ingredient.getImperial(), result.getImperial());
    }
}
