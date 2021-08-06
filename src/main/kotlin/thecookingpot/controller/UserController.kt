package thecookingpot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thecookingpot.service.*
import javax.transaction.Transactional

@RestController
@RequestMapping(path = ["/api/user"])
@Transactional
open class UserController @Autowired constructor(private val userService: UserService,
                                                 private val recipeService: RecipeService,
                                                 private val ingredientService: IngredientService,
                                                 private val shoppingListService: ShoppingListService,
                                                 private val inventoryService: InventoryService,
                                                 private val authService: AuthService
 ) {
    @DeleteMapping
    fun deleteUser(): ResponseEntity<*> {
        recipeService.deleteAllRecipes()
        shoppingListService.deleteAll()
        inventoryService.deleteAll()
        ingredientService.deleteAll()
        userService.deleteAccount()
        authService.disableUserSession()

        return ResponseEntity.status(202).body("Account deleted")
    }
}