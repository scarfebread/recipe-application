package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.IngredientDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.Recipe;
import recipeapplication.model.User;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeControllerTest
{
    private Errors errors;
    private Errors noErrors;
    private RecipeController recipeController;
    private RecipeService recipeService;
    private UserService userService;

    @Before
    public void setup()
    {
        errors = mock(Errors.class);
        noErrors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        when(noErrors.hasErrors()).thenReturn(false);

        recipeService = mock(RecipeService.class);
        userService = mock(UserService.class);

        recipeController = new RecipeController(recipeService, userService);
    }

    @Test
    public void shouldReturnInvalidRecipeWhenCreateRecipeHasErrors()
    {
        ResponseEntity response = recipeController.createRecipe(null, errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid recipe", response.getBody());
    }

    @Test
    public void shouldReturnRecipeWhenRecipeIsCreated()
    {
        CreateRecipeDto recipeDto = new CreateRecipeDto();
        Recipe recipe = new Recipe();

        when(recipeService.createRecipe(recipeDto)).thenReturn(recipe);

        ResponseEntity response = recipeController.createRecipe(recipeDto, noErrors);

        assertEquals(recipe, response.getBody());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    public void shouldGetRecipes()
    {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe());
        recipes.add(new Recipe());

        when(recipeService.getRecipes()).thenReturn(recipes);

        assertEquals(recipes, recipeController.getRecipes());
    }

    @Test
    public void shouldReturnNoRecipeSuppliedWhenDeleteRecipeWithNoId()
    {
        ResponseEntity response = recipeController.deleteRecipe(new RecipeDto());

        assertEquals("No recipe supplied", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenDeletingInvalidRecipe() throws RecipeDoesNotExistException
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        doThrow(RecipeDoesNotExistException.class).when(recipeService).deleteRecipe(recipeDto);

        ResponseEntity response = recipeController.deleteRecipe(recipeDto);

        assertEquals("Recipe does not exist", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldDeleteRecipeWithValidRequest() throws RecipeDoesNotExistException
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        ResponseEntity response = recipeController.deleteRecipe(recipeDto);

        verify(recipeService).deleteRecipe(recipeDto);

        assertEquals("Deleted successfully", response.getBody());
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnNoRecipeSuppliedWhenUpdateRecipeWithNoId()
    {
        ResponseEntity response = recipeController.updateRecipe(new RecipeDto());

        assertEquals("No recipe supplied", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenUpdatingInvalidRecipe() throws RecipeDoesNotExistException
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        doThrow(RecipeDoesNotExistException.class).when(recipeService).updateRecipe(recipeDto);

        ResponseEntity response = recipeController.updateRecipe(recipeDto);

        assertEquals("Recipe does not exist", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldUpdateRecipeWithValidRequest() throws RecipeDoesNotExistException
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        ResponseEntity response = recipeController.updateRecipe(recipeDto);

        verify(recipeService).updateRecipe(recipeDto);

        assertEquals("Updated", response.getBody());
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnNoRecipeSuppliedWhenSharingRecipeWithNoId()
    {
        ResponseEntity response = recipeController.shareRecipe(new RecipeDto());

        assertEquals("No recipe supplied", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnNoUserSuppliedWhenSharingRecipeWithNoId()
    {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);

        ResponseEntity response = recipeController.shareRecipe(recipeDto);

        assertEquals("No user supplied", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenSharingInvalidRecipe() throws Exception
    {
        String newUser = "newUser";

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setNewUser(newUser);

        User user = new User();

        when(userService.getUser(newUser)).thenReturn(user);
        doThrow(RecipeDoesNotExistException.class).when(recipeService).shareRecipe(recipeDto, user);

        ResponseEntity response = recipeController.shareRecipe(recipeDto);

        assertEquals("Recipe does not exist", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnUserDoesNotExistWhenSharingToInvalidUser() throws Exception
    {
        String newUser = "newUser";

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setNewUser(newUser);

        doThrow(UserNotFoundException.class).when(userService).getUser(newUser);

        ResponseEntity response = recipeController.shareRecipe(recipeDto);

        assertEquals("User does not exist", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldShareRecipeWithValidRequest() throws Exception
    {
        String newUser = "newUser";

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setNewUser(newUser);

        User user = new User();

        when(userService.getUser(newUser)).thenReturn(user);

        ResponseEntity response = recipeController.shareRecipe(recipeDto);

        verify(recipeService).shareRecipe(recipeDto, user);

        assertEquals("Created", response.getBody());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnInvalidIngredientWheInvalidIngredientSupplied()
    {
        ResponseEntity response = recipeController.addIngredient(null, errors);

        assertEquals("Invalid ingredient", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenRecipeDoesNotExist() throws Exception
    {
        IngredientDto ingredientDto = new IngredientDto();

        when(recipeService.addIngredient(ingredientDto)).thenThrow(RecipeDoesNotExistException.class);

        ResponseEntity response = recipeController.addIngredient(ingredientDto, noErrors);

        assertEquals("Recipe does not exist", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnIngredientWhenIngredientCreatedSuccessfully() throws Exception
    {
        IngredientDto ingredientDto = new IngredientDto();
        Ingredient ingredient = new Ingredient();
        ingredient.setImperial("imperial");
        ingredient.setMetric("metric");

        when(recipeService.addIngredient(ingredientDto)).thenReturn(ingredient);

        ResponseEntity response = recipeController.addIngredient(ingredientDto, noErrors);

        assertEquals(ingredient, response.getBody());
        assertEquals(202, response.getStatusCodeValue());
    }
}
