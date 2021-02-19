package thecookingpot.recipe.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import thecookingpot.recipe.service.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerTest
{
    private UserService userService;
    private RecipeService recipeService;
    private ShoppingListService shoppingListService;
    private InventoryService inventoryService;
    private IngredientService ingredientService;
    private AuthService authService;
    private UserController userController;

    @Before
    public void setup()
    {
        userService = mock(UserService.class);
        recipeService = mock(RecipeService.class);
        shoppingListService = mock(ShoppingListService.class);
        inventoryService = mock(InventoryService.class);
        ingredientService = mock(IngredientService.class);
        authService = mock(AuthService.class);

        userController = new UserController(
                userService, recipeService, ingredientService, shoppingListService, inventoryService, authService
        );
    }

    @Test
    public void shouldDeleteUserSuccessfully()
    {
        ResponseEntity responseEntity = userController.deleteUser();

        verify(userService).deleteAccount();
        verify(authService).disableUserSession();
        verify(recipeService).deleteAllRecipes();
        verify(shoppingListService).deleteAll();
        verify(inventoryService).deleteAll();
        verify(ingredientService).deleteAll();

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Account deleted", responseEntity.getBody());
    }
}
