package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.Ingredient
import thecookingpot.recipe.model.InventoryItem
import thecookingpot.recipe.model.User
import java.util.*
import javax.transaction.Transactional

interface InventoryRepository : JpaRepository<InventoryItem, Long> {
    fun findByUser(user: User): List<InventoryItem>
    fun findByIdAndUser(id: Long, user: User): InventoryItem?
    fun findByIngredientAndUser(ingredient: Ingredient, user: User): InventoryItem?
    fun findByIngredientDescription(ingredient: String): List<InventoryItem>

    @Transactional
    fun deleteAllByUser(user: User)
}