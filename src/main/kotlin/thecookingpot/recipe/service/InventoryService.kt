package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.recipe.dto.InventoryItemDto
import thecookingpot.recipe.exception.InventoryItemNotFoundException
import thecookingpot.recipe.model.Ingredient
import thecookingpot.recipe.model.InventoryItem
import thecookingpot.recipe.repository.IngredientRepository
import thecookingpot.recipe.repository.InventoryRepository
import thecookingpot.security.service.AuthService

@Service
class InventoryService @Autowired constructor(
        private val inventoryRepository: InventoryRepository,
        private val ingredientRepository: IngredientRepository,
        private val authService: AuthService
) {
    val inventory: List<InventoryItem>
        get() {
            return inventoryRepository.findByUser(
                    authService.loggedInUser
            )
        }

    val ingredients: List<String>
        get() = ingredientRepository.getIngredients(authService.loggedInUser)

    @Throws(InventoryItemNotFoundException::class)
    fun deleteInventoryItem(inventoryItemDto: InventoryItemDto) {
        inventoryRepository.delete(
                getInventoryItem(inventoryItemDto)
        )
    }

    fun createInventoryItem(inventoryItemDto: InventoryItemDto): InventoryItem {
        val inventoryItem = InventoryItem().apply {
            user = authService.loggedInUser
            ingredient = Ingredient(inventoryItemDto.ingredient, inventoryItemDto.quantity, authService.loggedInUser)
        }

        return inventoryRepository.save(inventoryItem)
    }

    fun createInventoryItem(ingredient: Ingredient) {
        // If a inventory item already exists for the same ingredient we will ignore the request to avoid duplicates
        inventoryRepository.findByIngredientAndUser(ingredient, authService.loggedInUser)?.let {
            return
        }

        val inventoryItem = InventoryItem().apply {
            user = authService.loggedInUser
            this.ingredient = ingredient
        }

        inventoryRepository.save(inventoryItem)
    }

    @Throws(InventoryItemNotFoundException::class)
    private fun getInventoryItem(inventoryItemDto: InventoryItemDto): InventoryItem {
        return inventoryRepository.findByIdAndUser(inventoryItemDto.id, authService.loggedInUser) ?: throw InventoryItemNotFoundException()
    }

    fun getSimilarIngredients(ingredient: Ingredient): List<Ingredient> {
        val inventoryItems = inventoryRepository.findByIngredientDescription(ingredient.description)

        return inventoryItems.map { item -> item.ingredient }
    }

    fun deleteAll() {
        inventoryRepository.deleteAllByUser(
                authService.loggedInUser
        )
    }
}