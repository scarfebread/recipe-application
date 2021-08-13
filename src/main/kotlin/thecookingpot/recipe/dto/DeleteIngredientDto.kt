package thecookingpot.recipe.dto

import kotlin.properties.Delegates

class DeleteIngredientDto {
    var ingredientId by Delegates.notNull<Long>()
    var recipeId by Delegates.notNull<Long>()
}