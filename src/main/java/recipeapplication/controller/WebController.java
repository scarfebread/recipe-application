package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Recipe;
import recipeapplication.service.AuthService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

@Controller
public class WebController
{
    private UserService userService;
    private RecipeService recipeService;
    private AuthService authService;

    @Autowired
    public WebController(UserService userService, RecipeService recipeService, AuthService authService)
    {
        this.userService = userService;
        this.recipeService = recipeService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLogin()
    {
        return "login.html";
    }

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("user", authService.getLoggedInUser().getUsername());
        model.addAttribute("recentlyViewed", recipeService.getRecentlyViewed());

        return "home.html";
    }

    @GetMapping("/signup")
    public String signup()
    {
        return "signup.html";
    }

    @GetMapping("/recipe")
    public String recipe(@RequestParam Long id, Model model)
    {
        try
        {
            Recipe recipe = recipeService.getRecipe(id);

            recipeService.addRecentlyViewed(recipe);

            model.addAttribute("recipe", recipe);
            model.addAttribute("recentlyViewed", recipeService.getRecentlyViewed());
            model.addAttribute("user", authService.getLoggedInUser().getUsername());

            return "recipe.html";
        }
        catch (RecipeDoesNotExistException e)
        {
            return "invalid_recipe.html";
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

    // TODO this could have a better name
    @GetMapping("/changePasswordLoggedIn")
    public String changePasswordLoggedIn(Model model)
    {
        model.addAttribute("user", authService.getLoggedInUser().getUsername());
        model.addAttribute("recentlyViewed", recipeService.getRecentlyViewed());

        return "change_password_logged_in.html";
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount(Model model)
    {
        model.addAttribute("user", authService.getLoggedInUser().getUsername());
        model.addAttribute("recentlyViewed", recipeService.getRecentlyViewed());

        return "delete_account.html";
    }
}
