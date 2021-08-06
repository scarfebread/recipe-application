package thecookingpot.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import thecookingpot.dto.*;
import thecookingpot.exception.IngredientDoesNotExistException;
import thecookingpot.exception.RecipeDoesNotExistException;
import thecookingpot.exception.SameUsernameException;
import thecookingpot.exception.UserNotFoundException;
import thecookingpot.model.Ingredient;
import thecookingpot.model.Recipe;
import thecookingpot.model.Step;
import thecookingpot.model.User;
import thecookingpot.service.InventoryService;
import thecookingpot.service.RecipeService;
import thecookingpot.service.UserService;

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
    private InventoryService inventoryService;
    private UserService userService;

    @Before
    public void setup()
    {
        errors = mock(Errors.class);
        noErrors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        when(noErrors.hasErrors()).thenReturn(false);

        recipeService = mock(RecipeService.class);
        inventoryService = mock(InventoryService.class);
        userService = mock(UserService.class);

        recipeController = new RecipeController(recipeService, inventoryService, userService);
    }

    @Test
    public void shouldReturnInvalidRecipeWhenCreateRecipeHasErrors()
    {
        ResponseEntity response = recipeController.createRecipe(new CreateRecipeDto(), errors);

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
        verify(userService).turnOffInstructions();

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
    public void shouldReturnBadRequestWhenSharingToSameUser() throws Exception
    {
        String newUser = "newUser";

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setNewUser(newUser);

        User user = new User();

        when(userService.getUser(newUser)).thenReturn(user);
        doThrow(SameUsernameException.class).when(recipeService).shareRecipe(recipeDto, user);

        ResponseEntity response = recipeController.shareRecipe(recipeDto);

        assertEquals("You already have this recipe", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
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
        ResponseEntity response = recipeController.addIngredient(new IngredientDto(), errors);

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

        List<Ingredient> similarIngredients = new ArrayList<>();
        similarIngredients.add(new Ingredient());

        when(recipeService.addIngredient(ingredientDto)).thenReturn(ingredient);
        when(inventoryService.getSimilarIngredients(ingredient)).thenReturn(similarIngredients);

        ResponseEntity response = recipeController.addIngredient(ingredientDto, noErrors);

        assertEquals(ingredient, response.getBody());
        assertEquals(similarIngredients, ((Ingredient) response.getBody()).getInventoryItems());
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnInvalidIngredientWhenHasErrors()
    {
        ResponseEntity response = recipeController.deleteIngredient(new DeleteIngredientDto(), errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid ingredient supplied", response.getBody());
    }

    @Test
    public void shouldReturnIngredientNotFoundWhenIngredientNotFound() throws Exception
    {
        DeleteIngredientDto ingredientDto = new DeleteIngredientDto();

        doThrow(IngredientDoesNotExistException.class).when(recipeService).deleteIngredient(ingredientDto);

        ResponseEntity response = recipeController.deleteIngredient(ingredientDto, noErrors);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ingredient not found", response.getBody());
    }

    @Test
    public void shouldDeleteIngredientWithValidRequest() throws Exception
    {
        DeleteIngredientDto ingredientDto = new DeleteIngredientDto();

        ResponseEntity response = recipeController.deleteIngredient(ingredientDto, noErrors);

        verify(recipeService).deleteIngredient(ingredientDto);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Ingredient deleted", response.getBody());
    }

    @Test
    public void shouldReturnInvalidStepWhenAddingInvalidStep()
    {
        ResponseEntity response = recipeController.addStep(new CreateStepDto(), errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid step", response.getBody());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenRecipeDoesNotExistWhenAddingStep() throws Exception
    {
        CreateStepDto stepDto = new CreateStepDto();

        when(recipeService.addStep(stepDto)).thenThrow(RecipeDoesNotExistException.class);

        ResponseEntity response = recipeController.addStep(stepDto, noErrors);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Recipe does not exist", response.getBody());
    }

    @Test
    public void shouldAddStepSuccessfully() throws Exception
    {
        CreateStepDto stepDto = new CreateStepDto();
        Step step = new Step();

        when(recipeService.addStep(stepDto)).thenReturn(step);

        ResponseEntity response = recipeController.addStep(stepDto, noErrors);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(step, response.getBody());
    }

    @Test
    public void shouldReturnInvalidStepWhenUpdatingInvalidStep()
    {
        ResponseEntity response = recipeController.updateStep(new UpdateStepDto(), errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid step supplied", response.getBody());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenRecipeDoesNotExistWhenUpdatingStep() throws Exception
    {
        UpdateStepDto stepDto = new UpdateStepDto();

        doThrow(RecipeDoesNotExistException.class).when(recipeService).updateStep(stepDto);

        ResponseEntity response = recipeController.updateStep(stepDto, noErrors);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Recipe not found", response.getBody());
    }

    @Test
    public void shouldUpdateStepSuccessfully() throws Exception
    {
        UpdateStepDto stepDto = new UpdateStepDto();

        ResponseEntity response = recipeController.updateStep(stepDto, noErrors);

        verify(recipeService).updateStep(stepDto);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Step updated", response.getBody());
    }

    @Test
    public void shouldReturnInvalidStepWhenDeletingInvalidStep()
    {
        ResponseEntity response = recipeController.deleteStep(new DeleteStepDto(), errors);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid step supplied", response.getBody());
    }

    @Test
    public void shouldReturnRecipeDoesNotExistWhenRecipeDoesNotExistWhenDeletingStep() throws Exception
    {
        DeleteStepDto stepDto = new DeleteStepDto();

        doThrow(RecipeDoesNotExistException.class).when(recipeService).deleteStep(stepDto);

        ResponseEntity response = recipeController.deleteStep(stepDto, noErrors);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Recipe not found", response.getBody());
    }

    @Test
    public void shouldDeleteStepSuccessfully() throws Exception
    {
        DeleteStepDto stepDto = new DeleteStepDto();

        ResponseEntity response = recipeController.deleteStep(stepDto, noErrors);

        verify(recipeService).deleteStep(stepDto);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Step deleted", response.getBody());
    }
}
