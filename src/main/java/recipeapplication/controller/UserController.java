package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeapplication.service.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/api/user")
@Transactional
public class UserController
{
    private UserService userService;
    private RecipeService recipeService;
    private ShoppingListService shoppingListService;
    private InventoryService inventoryService;
    private IngredientService ingredientService;
    private AuthService authService;

    @Autowired
    public UserController(UserService userService,
                          RecipeService recipeService,
                          IngredientService ingredientService,
                          ShoppingListService shoppingListService,
                          InventoryService inventoryService,
                          AuthService authService)
    {
        this.userService = userService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.shoppingListService = shoppingListService;
        this.inventoryService = inventoryService;
        this.authService = authService;
    }

    @DeleteMapping
    public ResponseEntity deleteUser()
    {
        recipeService.deleteAllRecipes();
        shoppingListService.deleteAll();
        inventoryService.deleteAll();
        ingredientService.deleteAll();
        userService.deleteAccount();
        authService.disableUserSession();

        return ResponseEntity.status(202).body("Account deleted");
    }
}
