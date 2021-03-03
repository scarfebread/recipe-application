package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.recipe.repository.IngredientRepository
import thecookingpot.security.service.AuthService
import javax.transaction.Transactional

@Service
@Transactional
open class IngredientService @Autowired constructor(private val ingredientRepository: IngredientRepository, private val authService: AuthService) {
    fun deleteAll() {
        ingredientRepository.deleteAllByUser(
                authService.loggedInUser
        )
    }
}