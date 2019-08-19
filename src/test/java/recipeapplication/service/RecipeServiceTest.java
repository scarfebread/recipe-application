package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import recipeapplication.dto.*;
import recipeapplication.exception.IngredientDoesNotExistException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.exception.SameUsernameException;
import recipeapplication.model.*;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecentlyViewedRepository;
import recipeapplication.repository.RecipeRepository;

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
    private RecentlyViewedRepository recentlyViewedRepository;
    private EntityManager entityManager;
    private RecipeService recipeService;
    private User loggedInUser;

    @Before
    public void setup()
    {
        recipeRepository = mock(RecipeRepository.class);
        ingredientRepository = mock(IngredientRepository.class);
        AuthService authService = mock(AuthService.class);
        recentlyViewedRepository = mock(RecentlyViewedRepository.class);
        entityManager = mock(EntityManager.class);

        loggedInUser = new User();
        loggedInUser.setUsername("testuser");
        loggedInUser.setId(1L);

        when(authService.getLoggedInUser()).thenReturn(loggedInUser);

        recipeService = new RecipeService(recipeRepository, ingredientRepository, authService, recentlyViewedRepository, entityManager);
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
        assertEquals(loggedInUser, recipe.getUser());
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

        when(recipeRepository.findByUser(loggedInUser)).thenReturn(recipes);

        assertEquals(recipes, recipeService.getRecipes());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenRecipeDoesNotExist() throws Exception
    {
        Long recipeId = 2L;

        when(recipeRepository.findByIdAndUser(recipeId, loggedInUser)).thenReturn(Optional.empty());

        recipeService.getRecipe(recipeId);
    }

    @Test
    public void shouldGetRecipeWhenRecipeExists() throws Exception
    {
        Recipe recipe = new Recipe();
        recipe.setId(3L);

        when(recipeRepository.findByIdAndUser(recipe.getId(), loggedInUser)).thenReturn(Optional.of(recipe));

        assertEquals(recipe, recipeService.getRecipe(recipe.getId()));
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenDeletingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUser(recipeDto.getId(), loggedInUser)).thenReturn(Optional.empty());

        recipeService.deleteRecipe(recipeDto);
    }

    @Test
    public void shouldDeleteRecipeSuccessfully() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(3L);

        Recipe recipe = new Recipe();
        recipe.setId(3L);

        when(recipeRepository.findByIdAndUser(recipe.getId(), loggedInUser)).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(recipeDto);

        verify(recipeRepository).deleteById(recipe.getId());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenUpdatingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUser(recipeDto.getId(), loggedInUser)).thenReturn(Optional.empty());

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

        when(recipeRepository.findByIdAndUser(recipeDto.getId(), loggedInUser)).thenReturn(Optional.of(new Recipe()));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        recipeService.updateRecipe(recipeDto);

        verify(recipeRepository).save(argumentCaptor.capture());

        Recipe recipe = argumentCaptor.getValue();

        assertEquals(NOTES, recipe.getNotes());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(TOTAL_TIME, recipe.getTotalTime());
        assertEquals(RATING, recipe.getRating());
        assertEquals(SERVES, recipe.getServes());
    }

    @Test(expected = RecipeDoesNotExistException.class)
    public void shouldThrowRecipeDoesNotExistExceptionWhenSharingRecipeThatDoesNotExist() throws Exception
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(2L);

        when(recipeRepository.findByIdAndUser(recipeDto.getId(), loggedInUser)).thenReturn(Optional.empty());

        recipeService.shareRecipe(recipeDto, new User());
    }

    @Test(expected = SameUsernameException.class)
    public void shouldThrowSameUsernameExceptionWhenSharingWithSameUser() throws Exception
    {
        User user = new User();
        user.setUsername(loggedInUser.getUsername());

        recipeService.shareRecipe(new RecipeDto(), user);
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
        ingredients.add(new Ingredient("ShoppingListItemDto", "Quantity", loggedInUser));

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(recipe, "Step"));

        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        when(recipeRepository.findByIdAndUser(recipeDto.getId(), loggedInUser)).thenReturn(Optional.of(recipe));

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
        assertEquals(userToShareWith, sharedRecipe.getUser());
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
        recentlyViewed1.setUser(loggedInUser);

        Recipe aRecentRecipe = new Recipe();
        aRecentRecipe.setId(2L);

        RecentlyViewed recentlyViewed2 = new RecentlyViewed();
        recentlyViewed2.setRecipe(aRecentRecipe);
        recentlyViewed2.setUser(loggedInUser);

        List<RecentlyViewed> recentlyViewed = new ArrayList<>();
        recentlyViewed.add(recentlyViewed1);
        recentlyViewed.add(recentlyViewed2);

        when(recentlyViewedRepository.findTop5ByUserOrderByIdDesc(loggedInUser)).thenReturn(recentlyViewed);

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUser(loggedInUser);

        ArgumentCaptor<RecentlyViewed> argumentCaptor = ArgumentCaptor.forClass(RecentlyViewed.class);

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository).save(argumentCaptor.capture());

        RecentlyViewed result = argumentCaptor.getValue();

        assertEquals(viewedRecipe, result.getRecipe());
        assertEquals(loggedInUser, result.getUser());
    }

    @Test
    public void shouldAddRecentlyViewedByIfNoRecentlyViewedRecipes()
    {
        Recipe mostRecentRecipe = new Recipe();
        mostRecentRecipe.setId(1L);

        when(recentlyViewedRepository.findTop5ByUserOrderByIdDesc(loggedInUser)).thenReturn(new ArrayList<>());

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUser(loggedInUser);

        ArgumentCaptor<RecentlyViewed> argumentCaptor = ArgumentCaptor.forClass(RecentlyViewed.class);

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository).save(argumentCaptor.capture());

        RecentlyViewed result = argumentCaptor.getValue();

        assertEquals(viewedRecipe, result.getRecipe());
        assertEquals(loggedInUser, result.getUser());
    }

    @Test
    public void shouldNotAddRecentlyViewedByIfMostRecentRecipe()
    {
        Recipe mostRecentRecipe = new Recipe();
        mostRecentRecipe.setId(2L);

        RecentlyViewed recentlyViewed1 = new RecentlyViewed();
        recentlyViewed1.setRecipe(mostRecentRecipe);
        recentlyViewed1.setUser(loggedInUser);

        Recipe aRecentRecipe = new Recipe();
        aRecentRecipe.setId(1L);

        RecentlyViewed recentlyViewed2 = new RecentlyViewed();
        recentlyViewed2.setRecipe(aRecentRecipe);
        recentlyViewed2.setUser(loggedInUser);

        List<RecentlyViewed> recentlyViewed = new ArrayList<>();
        recentlyViewed.add(recentlyViewed1);
        recentlyViewed.add(recentlyViewed2);

        when(recentlyViewedRepository.findTop5ByUserOrderByIdDesc(loggedInUser)).thenReturn(recentlyViewed);

        Recipe viewedRecipe =  new Recipe();
        viewedRecipe.setId(2L);
        viewedRecipe.setUser(loggedInUser);

        recipeService.addRecentlyViewed(viewedRecipe);

        verify(recentlyViewedRepository, never()).save(any());
    }

    @Test
    public void shouldGetRecentlyViewedSuccessfully()
    {
        List<RecentlyViewed> recentlyViewed = new ArrayList<>();

        recentlyViewed.add(new RecentlyViewed());
        recentlyViewed.add(new RecentlyViewed());

        when(recentlyViewedRepository.findTop5ByUserOrderByIdDesc(loggedInUser)).thenReturn(recentlyViewed);

        assertEquals(recentlyViewed, recipeService.getRecentlyViewed());
    }

    @Test
    public void shouldDeleteAllRecipesSuccessfully()
    {
        recipeService.deleteAllRecipes();

        verify(recipeRepository).deleteAllByUser(loggedInUser);
    }

    @Test
    public void shouldAddIngredientSuccessfully() throws Exception
    {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("ShoppingListItemDto");
        ingredientDto.setQuantity("Quantity");
        ingredientDto.setRecipe(1L);

        Recipe recipe = new Recipe();

        when(recipeRepository.findByIdAndUser(ingredientDto.getRecipe(), loggedInUser)).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        Ingredient result = recipeService.addIngredient(ingredientDto);

        verify(recipeRepository).save(argumentCaptor.capture());

        assertEquals(result, argumentCaptor.getValue().getIngredients().get(0));
        assertEquals(ingredientDto.getDescription(), result.getDescription());
        assertEquals(ingredientDto.getQuantity(), result.getMetric());
        assertEquals(ingredientDto.getQuantity(), result.getImperial());
        assertEquals(loggedInUser, result.getUser());
    }

    @Test(expected = IngredientDoesNotExistException.class)
    public void shouldThrowIngredientNotFoundWhenIngredientDoesNotExist() throws Exception
    {
        DeleteIngredientDto ingredientDto = new DeleteIngredientDto();
        ingredientDto.setIngredientId(12345L);
        ingredientDto.setRecipeId(54321L);

        when(ingredientRepository.findByIdAndUser(ingredientDto.getIngredientId(), loggedInUser)).thenReturn(Optional.empty());
        when(recipeRepository.findByIdAndUser(ingredientDto.getRecipeId(), loggedInUser)).thenReturn(Optional.of(new Recipe()));

        recipeService.deleteIngredient(ingredientDto);
    }

    @Test
    public void shouldDeleteIngredientWhenItExistsForTheRecipeSupplied() throws Exception
    {
        DeleteIngredientDto ingredientDto = new DeleteIngredientDto();
        ingredientDto.setIngredientId(12345L);
        ingredientDto.setRecipeId(54321L);

        Ingredient ingredient = new Ingredient();
        Ingredient anotherIngredient = new Ingredient();

        Recipe recipe = new Recipe();
        recipe.addIngredient(ingredient);
        recipe.addIngredient(anotherIngredient);

        when(ingredientRepository.findByIdAndUser(ingredientDto.getIngredientId(), loggedInUser)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.findByIdAndUser(ingredientDto.getRecipeId(), loggedInUser)).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        recipeService.deleteIngredient(ingredientDto);

        verify(recipeRepository).save(argumentCaptor.capture());

        Recipe result = argumentCaptor.getValue();

        assertEquals(1, result.getIngredients().size());
        assertEquals(anotherIngredient, result.getIngredients().get(0));
    }

    @Test
    public void shouldNotDeleteIngredientWhenItDoesNotExistsForTheRecipeSupplied() throws Exception
    {
        DeleteIngredientDto ingredientDto = new DeleteIngredientDto();
        ingredientDto.setIngredientId(12345L);
        ingredientDto.setRecipeId(54321L);

        Ingredient ingredient = new Ingredient();
        Ingredient anotherIngredient = new Ingredient();

        Recipe recipe = new Recipe();
        recipe.addIngredient(anotherIngredient);

        when(ingredientRepository.findByIdAndUser(ingredientDto.getIngredientId(), loggedInUser)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.findByIdAndUser(ingredientDto.getRecipeId(), loggedInUser)).thenReturn(Optional.of(recipe));

        recipeService.deleteIngredient(ingredientDto);

        verify(recipeRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteStepSuccessfully() throws Exception
    {
        Step step1 = new Step();
        step1.setId(1L);
        Step step2 = new Step();
        step2.setId(2L);
        Step step3 = new Step();
        step3.setId(3L);

        Recipe recipe = new Recipe();
        recipe.addStep(step1);
        recipe.addStep(step2);
        recipe.addStep(step3);

        DeleteStepDto stepDto = new DeleteStepDto();
        stepDto.setStepId(2L);
        stepDto.setRecipeId(4L);

        when(recipeRepository.findByIdAndUser(stepDto.getRecipeId(), loggedInUser)).thenReturn(Optional.of(recipe));

        recipeService.deleteStep(stepDto);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify(recipeRepository).save(argumentCaptor.capture());

        List<Step> result = argumentCaptor.getValue().getSteps();

        assertEquals(2, result.size());
        assertEquals(step1, result.get(0));
        assertEquals(step3, result.get(1));
    }

    @Test
    public void shouldUpdateStepSuccessfully() throws Exception
    {
        Step step1 = new Step();
        step1.setId(1L);
        Step step2 = new Step();
        step2.setId(2L);
        Step step3 = new Step();
        step3.setId(3L);

        Recipe recipe = new Recipe();
        recipe.addStep(step1);
        recipe.addStep(step2);
        recipe.addStep(step3);

        UpdateStepDto stepDto = new UpdateStepDto();
        stepDto.setId(2L);
        stepDto.setRecipe(4L);
        stepDto.setDescription("DESCRIPTION");

        when(recipeRepository.findByIdAndUser(stepDto.getRecipe(), loggedInUser)).thenReturn(Optional.of(recipe));

        recipeService.updateStep(stepDto);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify(recipeRepository).save(argumentCaptor.capture());

        List<Step> result = argumentCaptor.getValue().getSteps();

        assertNull(result.get(0).getDescription());
        assertEquals(stepDto.getDescription(), result.get(1).getDescription());
        assertNull(result.get(2).getDescription());
    }

    @Test
    public void shouldAddStepSuccessfully() throws Exception
    {
        CreateStepDto stepDto = new CreateStepDto();
        stepDto.setDescription("DESCRIPTION");
        stepDto.setRecipe(1L);

        Recipe recipe = new Recipe();

        when(recipeRepository.findByIdAndUser(stepDto.getRecipe(), loggedInUser)).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        Step result = recipeService.addStep(stepDto);

        verify(recipeRepository).save(argumentCaptor.capture());

        assertEquals(result, argumentCaptor.getValue().getSteps().get(0));
        assertEquals(stepDto.getDescription(), result.getDescription());
    }
}
