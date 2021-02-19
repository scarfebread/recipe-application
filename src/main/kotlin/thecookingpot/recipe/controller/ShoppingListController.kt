package thecookingpot.recipe.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import thecookingpot.recipe.dto.ShoppingListItemDto
import thecookingpot.recipe.exception.IngredientDoesNotExistException
import thecookingpot.recipe.exception.ShoppingListItemNotFoundException
import thecookingpot.recipe.model.ShoppingListItem
import thecookingpot.recipe.service.InventoryService
import thecookingpot.recipe.service.ShoppingListService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/shopping-list"])
class ShoppingListController @Autowired constructor(private val shoppingListService: ShoppingListService, private val inventoryService: InventoryService) {
    @GetMapping
    fun getShoppingListItems(): List<ShoppingListItem> {
        return shoppingListService.shoppingList
    }
    
    @PostMapping
    fun createShoppingListItem(@RequestBody shoppingListItemDto: @Valid ShoppingListItemDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid shopping list item")
        }

        return ResponseEntity
                .status(201)
                .body(shoppingListService.createShoppingListItem(shoppingListItemDto))
    }

    @DeleteMapping
    fun deleteShoppingListItem(@RequestBody shoppingListItemDto: ShoppingListItemDto): ResponseEntity<*> {
        try {
            shoppingListService.deleteShoppingListItem(shoppingListItemDto)
        } catch (e: ShoppingListItemNotFoundException) {
            return ResponseEntity.status(404).body("Shopping list item not found")
        }

        return ResponseEntity.status(202).body("Deleted successfully")
    }

    @PostMapping("/purchase")
    fun purchaseIngredient(@RequestBody shoppingListItemDto: ShoppingListItemDto): ResponseEntity<*> {
        try {
            val shoppingListItem = shoppingListService.getShoppingListItem(shoppingListItemDto)

            inventoryService.createInventoryItem(shoppingListItem.ingredient)
            shoppingListService.deleteShoppingListItem(shoppingListItemDto)
        } catch (e: ShoppingListItemNotFoundException) {
            return ResponseEntity.status(404).body("Shopping list item not found")
        }

        return ResponseEntity.status(201).body("Created")
    }

    @DeleteMapping("/remove/{ingredientId}")
    fun removeFromShoppingList(@PathVariable(value = "ingredientId") ingredientId: Long): ResponseEntity<*> {
        return try {
            shoppingListService.removeFromShoppingList(ingredientId)
            ResponseEntity.status(202).body("Removed successfully")
        } catch (e: ShoppingListItemNotFoundException) {
            ResponseEntity.status(404).body("Shopping list entry not found")
        }
    }

    @PostMapping("/add/{ingredientId}")
    fun addToShoppingList(@PathVariable(value = "ingredientId") ingredientId: Long): ResponseEntity<*> {
        return try {
            shoppingListService.addToShoppingList(ingredientId)
            ResponseEntity.status(201).body("Added successfully")
        } catch (e: IngredientDoesNotExistException) {
            ResponseEntity.status(404).body("Ingredient not found")
        }
    }
}