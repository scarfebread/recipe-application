package recipeapplication.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.RecentlyViewed;
import recipeapplication.model.Recipe;
import recipeapplication.model.User;
import recipeapplication.service.AuthService;
import recipeapplication.service.InventoryService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

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
    private AuthService authService;
    private List<RecentlyViewed> recentlyViewed;

    @Before
    public void setup() throws InvalidPasswordTokenException
    {
        userService = mock(UserService.class);
        recipeService = mock(RecipeService.class);
        authService = mock(AuthService.class);
        inventoryService = mock(InventoryService.class);

        User user = new User();
        user.setUsername(USERNAME);

        recentlyViewed = new ArrayList<>();
        recentlyViewed.add(new RecentlyViewed());
        recentlyViewed.add(new RecentlyViewed());

        doThrow(InvalidPasswordTokenException.class).when(userService).processPasswordResetToken(INVALID_TOKEN);
        when(recipeService.getRecentlyViewed()).thenReturn(recentlyViewed);
        when(authService.getLoggedInUser()).thenReturn(user);

        controller = new WebController(userService, recipeService, inventoryService, authService);
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

        List<String> ingredients = new ArrayList<>();
        ingredients.add("INGREDIENT1");
        ingredients.add("INGREDIENT2");

        when(recipeService.getRecipe(recipe.getId())).thenReturn(recipe);
        when(inventoryService.getIngredients()).thenReturn(ingredients);

        Model model = mock(Model.class);

        assertEquals("recipe.html", controller.recipe(recipe.getId(), model));

        verify(model).addAttribute("recipe", recipe);
        verify(model).addAttribute("recentlyViewed", recentlyViewed);
        verify(model).addAttribute("user", USERNAME);
        verify(model).addAttribute("ingredients", ingredients);
    }
}
