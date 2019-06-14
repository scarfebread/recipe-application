package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeapplication.service.AuthService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
public class UserController
{
    private UserService userService;
    private RecipeService recipeService;
    private AuthService authService;

    @Autowired
    public UserController(UserService userService, RecipeService recipeService, AuthService authService)
    {
        this.userService = userService;
        this.recipeService = recipeService;
        this.authService = authService;
    }

    @DeleteMapping
    public ResponseEntity deleteUser()
    {
        recipeService.deleteAllRecipes();
        userService.deleteAccount();
        authService.disableUserSession();

        return ResponseEntity.status(202).body("Account deleted");
    }
}
