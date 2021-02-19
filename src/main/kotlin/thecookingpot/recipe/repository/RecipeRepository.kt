package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.Recipe
import thecookingpot.recipe.model.User
import java.util.*

interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findByIdAndUser(id: Long, user: User): Optional<Recipe>
    fun findByUser(user: User): List<Recipe>
    fun deleteAllByUser(user: User)
}