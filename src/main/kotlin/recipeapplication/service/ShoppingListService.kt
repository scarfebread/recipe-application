package recipeapplication.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import recipeapplication.dto.ShoppingListItemDto
import recipeapplication.exception.IngredientDoesNotExistException
import recipeapplication.exception.ShoppingListItemNotFoundException
import recipeapplication.model.Ingredient
import recipeapplication.model.ShoppingListItem
import recipeapplication.repository.IngredientRepository
import recipeapplication.repository.ShoppingListRepository

@Service
class ShoppingListService @Autowired constructor(
        private val shoppingListRepository: ShoppingListRepository,
        private val ingredientRepository: IngredientRepository,
        private val authService: AuthService
) {
    val shoppingList: List<ShoppingListItem>
        get() {
            return shoppingListRepository.findByUser(
                    authService.loggedInUser
            )
        }

    @Throws(ShoppingListItemNotFoundException::class)
    fun deleteShoppingListItem(shoppingListItemDto: ShoppingListItemDto) {
        shoppingListRepository.delete(
                getShoppingListItem(shoppingListItemDto)
        )
    }

    @Throws(ShoppingListItemNotFoundException::class)
    fun getShoppingListItem(shoppingListItemDto: ShoppingListItemDto): ShoppingListItem {
        return shoppingListRepository.findByIdAndUser(shoppingListItemDto.id, authService.loggedInUser).orElseThrow { ShoppingListItemNotFoundException() }
    }

    fun createShoppingListItem(shoppingListItemDto: ShoppingListItemDto): ShoppingListItem {
        return shoppingListRepository.save(
                ShoppingListItem().apply {
                    user = authService.loggedInUser
                    ingredient = Ingredient(shoppingListItemDto.ingredient, shoppingListItemDto.quantity, authService.loggedInUser)
                }
        )
    }

    @Throws(IngredientDoesNotExistException::class)
    fun addToShoppingList(ingredientId: Long) {
        shoppingListRepository.save(
                ShoppingListItem().apply {
                    user = authService.loggedInUser
                    ingredient = ingredientRepository.findByIdAndUser(ingredientId, authService.loggedInUser).orElseThrow { IngredientDoesNotExistException() }
                }
        )
    }

    @Throws(ShoppingListItemNotFoundException::class)
    fun removeFromShoppingList(ingredientId: Long) {
        shoppingListRepository.delete(
                shoppingListRepository.findByIngredientIdAndUser(ingredientId, authService.loggedInUser).orElseThrow { ShoppingListItemNotFoundException() }
        )
    }

    fun isInShoppingList(ingredient: Ingredient): Boolean {
        return shoppingListRepository.findByIngredient(ingredient).isPresent
    }

    fun deleteAll() {
        shoppingListRepository.deleteAllByUser(
                authService.loggedInUser
        )
    }
}