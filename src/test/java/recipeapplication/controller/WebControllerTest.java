package recipeapplication.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.*;
import recipeapplication.service.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebControllerTest
{
    private static final String VALID_TOKEN = "VALID_TOKEN";
    private static final String INVALID_TOKEN = "INVALID_TOKEN";
    private static final String USERNAME = "USERNAME";

    private WebController controller;
    private RecipeService recipeService;
    private UserService userService;
    private InventoryService inventoryService;
    private ShoppingListService shoppingListService;
    private AuthService authService;
    private List<RecentlyViewed> recentlyViewed;
    private List<String> autoCompleteIngredients;

    @Before
    public void setup() throws InvalidPasswordTokenException
    {
        userService = mock(UserService.class);
        recipeService = mock(RecipeService.class);
        authService = mock(AuthService.class);
        inventoryService = mock(InventoryService.class);
        shoppingListService = mock(ShoppingListService.class);

        User user = new User();
        user.setUsername(USERNAME);
        user.setNewUser(true);

        recentlyViewed = new ArrayList<>();
        recentlyViewed.add(new RecentlyViewed());
        recentlyViewed.add(new RecentlyViewed());

        autoCompleteIngredients = new ArrayList<>();
        autoCompleteIngredients.add("INGREDIENT1");
        autoCompleteIngredients.add("INGREDIENT2");

        doThrow(InvalidPasswordTokenException.class).when(userService).processPasswordResetToken(INVALID_TOKEN);
        when(recipeService.getRecentlyViewed()).thenReturn(recentlyViewed);
        when(authService.getLoggedInUser()).thenReturn(user);

        controller = new WebController(userService, recipeService, inventoryService, shoppingListService, authService);
    }

    @Test
    public void shouldGetHomeWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.home(model);

        assertEquals("home.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldGetChangePasswordTemplateForValidChangePasswordToken()
    {
        assertEquals("change_password.html", controller.changePassword(VALID_TOKEN));
    }

    @Test
    public void shouldGetErrorTemplateForInvalidChangePasswordToken()
    {
        assertEquals("invalid_token.html", controller.changePassword(INVALID_TOKEN));
    }

    @Test
    public void shouldGetDeletePageWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.deleteAccount(model);

        assertEquals("delete_account.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldGetChangePasswordPageWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.changePasswordLoggedIn(model);

        assertEquals("change_password_logged_in.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldReturnInvalidRecipePageWhenRecipeDoesNotExist() throws Exception
    {
        when(recipeService.getRecipe(any())).thenThrow(RecipeDoesNotExistException.class);

        assertEquals("invalid_recipe.html", controller.recipe(1L, mock(Model.class)));
    }

    @Test
    public void shouldReturnRecipePageWithModelAttributesForValidRecipe() throws Exception
    {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.getRecipe(recipe.getId())).thenReturn(recipe);
        when(inventoryService.getIngredients()).thenReturn(autoCompleteIngredients);

        Model model = mock(Model.class);

        assertEquals("recipe.html", controller.recipe(recipe.getId(), model));

        verify(model).addAttribute("recipe", recipe);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("ingredients", autoCompleteIngredients);
        verify(model).addAttribute("displayInstructions", true);
    }

    @Test
    public void shouldReturnShoppingListTemplateWithModelAttributes()
    {
        List<ShoppingListItem> shoppingList = new ArrayList<>();
        shoppingList.add(new ShoppingListItem());
        shoppingList.add(new ShoppingListItem());

        when(shoppingListService.getShoppingList()).thenReturn(shoppingList);
        when(inventoryService.getIngredients()).thenReturn(autoCompleteIngredients);

        Model model = mock(Model.class);

        assertEquals("shopping_list.html", controller.shoppingList(model));

        verify(model).addAttribute("recentlyViewed", recentlyViewed);
        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("shoppingList", shoppingList);
        verify(model).addAttribute("ingredients", autoCompleteIngredients);
    }

    @Test
    public void shouldReturnInventoryTemplateWithModelAttributes()
    {
        List<InventoryItem> inventory = new ArrayList<>();
        inventory.add(new InventoryItem());
        inventory.add(new InventoryItem());

        when(inventoryService.getInventory()).thenReturn(inventory);
        when(inventoryService.getIngredients()).thenReturn(autoCompleteIngredients);

        Model model = mock(Model.class);

        assertEquals("inventory.html", controller.inventory(model));

        verify(model).addAttribute("recentlyViewed", recentlyViewed);
        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("inventory", inventory);
        verify(model).addAttribute("ingredients", autoCompleteIngredients);
    }
}
