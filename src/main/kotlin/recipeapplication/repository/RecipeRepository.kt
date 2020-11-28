package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import recipeapplication.model.Recipe
import recipeapplication.model.User
import java.util.*

interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findByIdAndUser(id: Long, user: User): Optional<Recipe>
    fun findByUser(user: User): List<Recipe>
    fun deleteAllByUser(user: User)
}