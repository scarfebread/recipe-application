package thecookingpot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.dto.InventoryItemDto
import thecookingpot.exception.InventoryItemNotFoundException
import thecookingpot.model.Ingredient
import thecookingpot.model.InventoryItem
import thecookingpot.repository.IngredientRepository
import thecookingpot.repository.InventoryRepository

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
        if (inventoryRepository.findByIngredientAndUser(ingredient, authService.loggedInUser).isPresent) {
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
        val inventoryItem = inventoryRepository.findByIdAndUser(inventoryItemDto.id, authService.loggedInUser)

        if (!inventoryItem.isPresent) {
            throw InventoryItemNotFoundException()
        }

        return inventoryItem.get()
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