package thecookingpot.recipe.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import thecookingpot.recipe.dto.*
import thecookingpot.recipe.exception.IngredientDoesNotExistException
import thecookingpot.recipe.exception.RecipeDoesNotExistException
import thecookingpot.recipe.exception.SameUsernameException
import thecookingpot.recipe.exception.UserNotFoundException
import thecookingpot.recipe.model.Recipe
import thecookingpot.recipe.service.InventoryService
import thecookingpot.recipe.service.RecipeService
import thecookingpot.recipe.service.UserService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/recipe"])
class RecipeController @Autowired constructor(private val recipeService: RecipeService, private val inventoryService: InventoryService, private val userService: UserService) {
    @PostMapping
    fun createRecipe(@RequestBody recipeDto: @Valid CreateRecipeDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid recipe")
        }

        return ResponseEntity
                .status(201)
                .body(recipeService.createRecipe(recipeDto))
    }

    @get:GetMapping
    val recipes: List<Recipe>
        get() = recipeService.recipes

    @DeleteMapping
    fun deleteRecipe(@RequestBody recipeDto: RecipeDto): ResponseEntity<*> {
        if (recipeDto.id == null) {
            return ResponseEntity.status(400).body("No recipe supplied")
        }

        try {
            recipeService.deleteRecipe(recipeDto)
        } catch (e: RecipeDoesNotExistException) {
            return ResponseEntity.status(404).body("Recipe does not exist")
        }

        return ResponseEntity.status(202).body("Deleted successfully")
    }

    @PutMapping
    fun updateRecipe(@RequestBody recipeDto: RecipeDto): ResponseEntity<*> {
        if (recipeDto.id == null) {
            return ResponseEntity.status(400).body("No recipe supplied")
        }

        return try {
            recipeService.updateRecipe(recipeDto)
            userService.turnOffInstructions()
            ResponseEntity.status(202).body("Updated")
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe does not exist")
        }
    }

    @PutMapping(path = ["/ingredient"])
    fun addIngredient(@RequestBody ingredientDto: @Valid IngredientDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid ingredient")
        }

        userService.turnOffInstructions()

        return try {
            val ingredient = recipeService.addIngredient(ingredientDto).apply {
                inventoryItems = inventoryService.getSimilarIngredients(this)
            }

            ResponseEntity.status(202).body(ingredient)
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe does not exist")
        }
    }

    @PutMapping(path = ["/delete-ingredient"])
    fun deleteIngredient(@RequestBody ingredientDto: @Valid DeleteIngredientDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid ingredient supplied")
        }

        return try {
            recipeService.deleteIngredient(ingredientDto)
            ResponseEntity.status(202).body("Ingredient deleted")
        } catch (e: IngredientDoesNotExistException) {
            ResponseEntity.status(404).body("Ingredient not found")
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Ingredient not found")
        }
    }

    @PutMapping(path = ["/add-step"])
    fun addStep(@RequestBody stepDto: @Valid CreateStepDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid step")
        }

        return try {
            userService.turnOffInstructions()
            ResponseEntity
                    .status(202)
                    .body(recipeService.addStep(stepDto))
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe does not exist")
        }
    }

    @PutMapping(path = ["/delete-step"])
    fun deleteStep(@RequestBody stepDto: @Valid DeleteStepDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid step supplied")
        }

        return try {
            recipeService.deleteStep(stepDto)
            ResponseEntity.status(202).body("Step deleted")
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe not found")
        }
    }

    @PutMapping(path = ["/update-step"])
    fun updateStep(@RequestBody stepDto: @Valid UpdateStepDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid step supplied")
        }

        return try {
            recipeService.updateStep(stepDto)
            ResponseEntity.status(202).body("Step updated")
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe not found")
        }
    }

    @PostMapping(path = ["/share"])
    fun shareRecipe(@RequestBody recipeDto: RecipeDto): ResponseEntity<*> {
        if (recipeDto.id == null) {
            return ResponseEntity.status(400).body("No recipe supplied")
        }

        if (recipeDto.newUser == null) {
            return ResponseEntity.status(400).body("No user supplied")
        }

        return try {
            val user = userService.getUser(recipeDto.newUser!!)
            recipeService.shareRecipe(recipeDto, user)
            ResponseEntity.status(201).body("Created")
        } catch (e: RecipeDoesNotExistException) {
            ResponseEntity.status(404).body("Recipe does not exist")
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(404).body("User does not exist")
        } catch (e: SameUsernameException) {
            ResponseEntity.status(400).body("You already have this recipe")
        }
    }
}