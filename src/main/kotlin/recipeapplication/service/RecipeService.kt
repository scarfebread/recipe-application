package recipeapplication.service

import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import recipeapplication.dto.*
import recipeapplication.exception.IngredientDoesNotExistException
import recipeapplication.exception.RecipeDoesNotExistException
import recipeapplication.exception.SameUsernameException
import recipeapplication.model.*
import recipeapplication.repository.IngredientRepository
import recipeapplication.repository.RecentlyViewedRepository
import recipeapplication.repository.RecipeRepository
import recipeapplication.utility.combineCookAndPrepTime
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
@Transactional
open class RecipeService @Autowired constructor(
        private val recipeRepository: RecipeRepository,
        private val ingredientRepository: IngredientRepository,
        private val authService: AuthService,
        private val recentlyViewedRepository: RecentlyViewedRepository,
        private val entityManager: EntityManager
) {
    companion object {
        private const val DEFAULT_DIFFICULTY = "Medium"
        private const val DEFAULT_TIME = "00:00"
        private const val DEFAULT_RATING = 0L
        private const val DEFAULT_SERVES = 1L
    }

    fun createRecipe(createRecipeDto: CreateRecipeDto): Recipe {
        return recipeRepository.save(
                Recipe().apply {
                    title = createRecipeDto.title
                    user = authService.loggedInUser
                    rating = DEFAULT_RATING
                    serves = DEFAULT_SERVES
                    difficulty = DEFAULT_DIFFICULTY
                    totalTime = DEFAULT_TIME
                    cookTime = DEFAULT_TIME
                    prepTime = DEFAULT_TIME
                }
        )
    }

    val recipes: List<Recipe>
        get() {
            return recipeRepository.findByUser(
                    authService.loggedInUser
            )
        }

    @Throws(RecipeDoesNotExistException::class)
    fun getRecipe(id: Long): Recipe {
        val result = recipeRepository.findByIdAndUser(id, authService.loggedInUser)

        if (!result.isPresent) {
            throw RecipeDoesNotExistException()
        }

        return result.get()
    }

    @Throws(RecipeDoesNotExistException::class)
    fun deleteRecipe(recipeDto: RecipeDto) {
        getRecipe(recipeDto.id)
        recipeRepository.deleteById(recipeDto.id)
    }

    @Throws(RecipeDoesNotExistException::class)
    fun updateRecipe(recipeDto: RecipeDto) {
        recipeRepository.save(
                getRecipe(recipeDto.id).apply {
                    notes = recipeDto.notes
                    rating = recipeDto.rating
                    serves = recipeDto.serves
                    cookTime = recipeDto.cookTime
                    prepTime = recipeDto.prepTime
                    totalTime = combineCookAndPrepTime(recipeDto.cookTime, recipeDto.prepTime)
                    difficulty = recipeDto.difficulty
                }
        )
    }

    @Throws(RecipeDoesNotExistException::class, SameUsernameException::class)
    fun shareRecipe(recipeDto: RecipeDto, user: User) {
        if (authService.loggedInUser.username == user.username) {
            throw SameUsernameException()
        }

        val recipe = getRecipe(recipeDto.id)

        // Clone the existing recipe
        Hibernate.initialize(recipe.ingredients)
        Hibernate.initialize(recipe.steps)
        entityManager.detach(recipe)

        recipe.apply {
            id = null
            sharedBy = authService.loggedInUser.username
            setUser(user)
            ingredients = recipe.ingredients.map { ingredient -> Ingredient(ingredient.description, ingredient.metric, user) }
            steps = recipe.steps.map { step -> Step(step.description) }
        }

        recipeRepository.save(recipe)
    }

    fun addRecentlyViewed(recipe: Recipe) {
        if (recentlyViewed.isEmpty() || recentlyViewed[0].recipe.id != recipe.id) {
            recentlyViewedRepository.save(
                    RecentlyViewed().apply {
                        setRecipe(recipe)
                        user = recipe.user
                    }
            )
        }
    }

    val recentlyViewed: List<RecentlyViewed>
        get() {
            return recentlyViewedRepository.findTop5ByUserOrderByIdDesc(
                    authService.loggedInUser
            )
        }

    fun deleteAllRecipes() {
        recipeRepository.deleteAllByUser(
                authService.loggedInUser
        )
    }

    @Throws(RecipeDoesNotExistException::class)
    fun addIngredient(ingredientDto: IngredientDto): Ingredient {
        val recipe = getRecipe(ingredientDto.recipe).apply {
            addIngredient(
                    Ingredient(ingredientDto.description, ingredientDto.quantity, authService.loggedInUser)
            )
        }

        recipeRepository.save(recipe)

        // Simply returning ingredient does not set the ID required by the front end
        return recipe.ingredients.last()
    }

    @Throws(RecipeDoesNotExistException::class)
    fun addStep(stepDto: CreateStepDto): Step {
        val recipe = getRecipe(stepDto.recipe).apply {
            addStep(
                    Step(stepDto.description)
            )
        }

        recipeRepository.save(recipe)

        // Simply returning the step does not set the ID required by the front end
        return recipe.steps.last()
    }

    @Throws(RecipeDoesNotExistException::class, IngredientDoesNotExistException::class)
    fun deleteIngredient(ingredientDto: DeleteIngredientDto) {
        val recipe = getRecipe(ingredientDto.recipeId)
        val ingredient = ingredientRepository.findByIdAndUser(ingredientDto.ingredientId, authService.loggedInUser)

        if (!ingredient.isPresent) { // TODO can Kotlin remove the need for Optional?
            throw IngredientDoesNotExistException()
        }

        if (recipe.ingredients.remove(ingredient.get())) {
            recipeRepository.save(recipe)
        }
    }

    @Throws(RecipeDoesNotExistException::class)
    fun deleteStep(stepDto: DeleteStepDto) {
        val recipe = getRecipe(stepDto.recipeId)
        val step = recipe.steps.firstOrNull { step -> step.id == stepDto.stepId }

        if (step != null) { // TODO sounds like this should throw an exception rather than be null safe
            recipe.steps.remove(step)
            recipeRepository.save(recipe)
        }
    }

    @Throws(RecipeDoesNotExistException::class)
    fun updateStep(stepDto: UpdateStepDto) {
        val recipe = getRecipe(stepDto.recipe)
        val step = recipe.steps.firstOrNull { step -> step.id == stepDto.id }

        if (step != null) { // TODO sounds like this should throw an exception rather than be null safe
            step.description = stepDto.description
            recipeRepository.save(recipe)
        }
    }
}