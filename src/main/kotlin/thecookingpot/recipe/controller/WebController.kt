package thecookingpot.recipe.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import thecookingpot.recipe.exception.InvalidPasswordTokenException
import thecookingpot.recipe.exception.RecipeDoesNotExistException
import thecookingpot.recipe.service.*

@Controller
class WebController // TODO I should have a custom error page
@Autowired constructor(
        private val userService: UserService,
        private val recipeService: RecipeService,
        private val inventoryService: InventoryService,
        private val shoppingListService: ShoppingListService,
        private val authService: AuthService) {
    @GetMapping("/login")
    fun login(): String {
        return "login.html"
    }

    @GetMapping("/")
    fun home(model: Model): String {
        model.apply {
            addAttribute("user", authService.loggedInUser.username)
            addAttribute("recentlyViewed", recipeService.recentlyViewed)
        }
        
        return "home.html"
    }

    // TODO unfinished react implementation
    @GetMapping("/react")
    fun homeReact(model: Model): String {
        model.apply {
            addAttribute("loggedInUser", authService.loggedInUser.username)
            addAttribute("recipes", recipeService.recipes)
            addAttribute("recentlyViewedRecipes", recipeService.recentlyViewed)
        }

        return "home_react.html"
    }

    @GetMapping("/signup")
    fun signup(): String {
        return "signup.html"
    }

    @GetMapping("/recipe")
    fun recipe(@RequestParam id: Long, model: Model): String {
        return try {
            val recipe = recipeService.getRecipe(id)

            recipeService.addRecentlyViewed(recipe)
            
            recipe.ingredients.forEach { ingredient ->
                ingredient.isInShoppingList = shoppingListService.isInShoppingList(ingredient)
                ingredient.inventoryItems = inventoryService.getSimilarIngredients(ingredient)
            }
            
            model.apply {
                addAttribute("recipe", recipe)
                addAttribute("recentlyViewed", recipeService.recentlyViewed)
                addAttribute("user", authService.loggedInUser.username)
                addAttribute("ingredients", inventoryService.ingredients)
                addAttribute("displayInstructions", authService.loggedInUser.newUser)
            }
            
            "recipe.html"
        } catch (e: RecipeDoesNotExistException) {
            "invalid-recipe.html"
        }
    }

    @GetMapping("/reset-password")
    fun resetPassword(): String {
        return "reset-password.html"
    }

    @GetMapping("/change-password-with-token")
    fun changePassword(@RequestParam token: String): String {
        return try {
            userService.processPasswordResetToken(token)
            "change-password-with-token.html"
        } catch (e: InvalidPasswordTokenException) {
            "invalid-token.html"
        }
    }

    @GetMapping("/change-password")
    fun changePasswordLoggedIn(model: Model): String {
        model.apply {
            addAttribute("loggedInUser", authService.loggedInUser.username)
            addAttribute("recentlyViewedRecipes", recipeService.recentlyViewed)
        }

        return "change-password.html"
    }

    @GetMapping("/delete-account")
    fun deleteAccount(model: Model): String {
        model.apply {
            addAttribute("user", authService.loggedInUser.username)
            addAttribute("recentlyViewed", recipeService.recentlyViewed)
        }

        return "delete-account.html"
    }

    @GetMapping("/shopping-list")
    fun shoppingList(model: Model): String {
        model.apply {
            addAttribute("user", authService.loggedInUser.username)
            addAttribute("recentlyViewed", recipeService.recentlyViewed)
            addAttribute("shoppingList", shoppingListService.shoppingList)
            addAttribute("ingredients", inventoryService.ingredients)
        }

        return "shopping-list.html"
    }

    @GetMapping("/inventory")
    fun inventory(model: Model): String {
        model.apply {
            addAttribute("user", authService.loggedInUser.username)
            addAttribute("recentlyViewed", recipeService.recentlyViewed)
            addAttribute("inventory", inventoryService.inventory)
            addAttribute("ingredients", inventoryService.ingredients)
        }

        return "inventory.html"
    }
}