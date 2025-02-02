package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import recipeapplication.model.Ingredient
import recipeapplication.model.InventoryItem
import recipeapplication.model.User
import java.util.*
import javax.transaction.Transactional

interface InventoryRepository : JpaRepository<InventoryItem?, Long?> {
    fun findByUser(user: User): List<InventoryItem>
    fun findByIdAndUser(id: Long, user: User): Optional<InventoryItem>
    fun findByIngredientAndUser(ingredient: Ingredient, user: User): Optional<InventoryItem>
    fun findByIngredientDescription(ingredient: String): List<InventoryItem>

    @Transactional
    fun deleteAllByUser(user: User)
}