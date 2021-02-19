package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.Ingredient
import thecookingpot.recipe.model.ShoppingListItem
import thecookingpot.recipe.model.User
import java.util.*

interface ShoppingListRepository : JpaRepository<ShoppingListItem, Long> {
    fun findByUser(user: User): List<ShoppingListItem>
    fun findByIdAndUser(id: Long, user: User): Optional<ShoppingListItem>
    fun findByIngredientIdAndUser(ingredientId: Long, user: User): Optional<ShoppingListItem>
    fun findByIngredient(ingredient: Ingredient): Optional<ShoppingListItem>
    fun deleteAllByUser(user: User)
}