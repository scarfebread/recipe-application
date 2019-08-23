package recipeapplication.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;

import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.*;
import recipeapplication.service.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        assertEquals("change-password-with-token.html", controller.changePassword(VALID_TOKEN));
    }

    @Test
    public void shouldGetErrorTemplateForInvalidChangePasswordToken()
    {
        assertEquals("invalid-token.html", controller.changePassword(INVALID_TOKEN));
    }

    @Test
    public void shouldGetDeletePageWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.deleteAccount(model);

        assertEquals("delete-account.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldGetChangePasswordPageWithModelAttributes()
    {
        Model model = mock(Model.class);

        String result = controller.changePasswordLoggedIn(model);

        assertEquals("change-password.html", result);

        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
    }

    @Test
    public void shouldReturnInvalidRecipePageWhenRecipeDoesNotExist() throws Exception
    {
        when(recipeService.getRecipe(any())).thenThrow(RecipeDoesNotExistException.class);

        assertEquals("invalid-recipe.html", controller.recipe(1L, mock(Model.class)));
    }

    @Test
    public void shouldReturnRecipePageWithModelAttributesForValidRecipe() throws Exception
    {
        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();

        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setIngredient(ingredient2);
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();
        shoppingListItems.add(shoppingListItem);

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        when(recipeService.getRecipe(recipe.getId())).thenReturn(recipe);
        when(inventoryService.getIngredients()).thenReturn(autoCompleteIngredients);

        when(shoppingListService.isInShoppingList(ingredient1)).thenReturn(false);
        when(shoppingListService.isInShoppingList(ingredient2)).thenReturn(true);

        Ingredient similarIngredient = new Ingredient();
        List<Ingredient> similarIngredients = Collections.singletonList(similarIngredient);

        when(inventoryService.getSimilarIngredients(ingredient1)).thenReturn(similarIngredients);
        when(inventoryService.getSimilarIngredients(ingredient2)).thenReturn(Collections.emptyList());

        Model model = mock(Model.class);

        assertEquals("recipe.html", controller.recipe(recipe.getId(), model));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify(model).addAttribute(eq("recipe"), argumentCaptor.capture());
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("ingredients", autoCompleteIngredients);
        verify(model).addAttribute("displayInstructions", true);

        List<Ingredient> resultIngredients = argumentCaptor.getValue().getIngredients();

        assertFalse(resultIngredients.get(0).isInShoppingList());
        assertTrue(resultIngredients.get(1).isInShoppingList());
        assertEquals(similarIngredients, resultIngredients.get(0).getInventoryItems());
        assertEquals(Collections.emptyList(), resultIngredients.get(1).getInventoryItems());
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

        assertEquals("shopping-list.html", controller.shoppingList(model));

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
