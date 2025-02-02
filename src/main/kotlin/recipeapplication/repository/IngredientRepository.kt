package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import recipeapplication.model.Ingredient
import recipeapplication.model.User
import java.util.*

interface IngredientRepository : JpaRepository<Ingredient?, Long?> {
    @Query("SELECT DISTINCT description FROM Ingredient WHERE user_id = ?1")
    fun getIngredients(user: User): List<String>

    fun findByIdAndUser(id: Long, user: User): Optional<Ingredient>

    fun deleteAllByUser(user: User)
}