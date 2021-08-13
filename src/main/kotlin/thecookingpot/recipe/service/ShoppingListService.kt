package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.auth.service.AuthService
import thecookingpot.recipe.dto.ShoppingListItemDto
import thecookingpot.recipe.exception.IngredientDoesNotExistException
import thecookingpot.recipe.exception.ShoppingListItemNotFoundException
import thecookingpot.recipe.model.Ingredient
import thecookingpot.recipe.model.ShoppingListItem
import thecookingpot.recipe.repository.IngredientRepository
import thecookingpot.recipe.repository.ShoppingListRepository

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