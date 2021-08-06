package thecookingpot.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.model.Ingredient
import thecookingpot.model.ShoppingListItem
import thecookingpot.model.User
import java.util.*

interface ShoppingListRepository : JpaRepository<ShoppingListItem, Long> {
    fun findByUser(user: User): List<ShoppingListItem>
    fun findByIdAndUser(id: Long, user: User): Optional<ShoppingListItem>
    fun findByIngredientIdAndUser(ingredientId: Long, user: User): Optional<ShoppingListItem>
    fun findByIngredient(ingredient: Ingredient): Optional<ShoppingListItem>
    fun deleteAllByUser(user: User)
}