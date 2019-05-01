package recipeapplication.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.Recipe;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

@Controller
public class WebController
{
    private UserService userService;
    private RecipeService recipeService;

    @Autowired
    public WebController(UserService userService, RecipeService recipeService)
    {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping("/login")
    public String getLogin()
    {
        return "login.html";
    }

    @GetMapping("/")
    public String index()
    {
        return "home.html";
    }

    @GetMapping("/signup")
    public String signup(WebRequest request, Model model)
    {
        model.addAttribute("user", new UserDto());

        return "signup.html";
    }

    @GetMapping("/recipe")
    public String recipe(@RequestParam Long id, Model model)
    {
        try
        {
            Recipe recipe = recipeService.getRecipe(id);

            List<String> ingredients = new ArrayList<>();

            for (Ingredient ingredient : recipe.getIngredients())
            {
                ingredients.add(ingredient.getName());
            }

            model.addAttribute("recipe", recipe);
            model.addAttribute("ingredients", ingredients);

            return "recipe.html";
        }
        catch (RecipeDoesNotExistException e)
        {
            return "recipeDoesNotExist.html";
        }
    }

    @GetMapping("/resetPassword")
    public String resetPassword()
    {
        return "reset_password.html";
    }

    @GetMapping("/changePassword")
    public String changePassword(@RequestParam String token)
    {
        try
        {
            userService.processPasswordResetToken(token);

            return "change_password.html";
        }
        catch (InvalidPasswordTokenException e)
        {
            return "invalid_token.html";
        }
    }
}
