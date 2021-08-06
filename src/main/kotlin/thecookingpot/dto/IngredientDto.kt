package thecookingpot.dto

import javax.validation.constraints.NotEmpty
import kotlin.properties.Delegates

class IngredientDto {
    lateinit var description: @NotEmpty String
    lateinit var quantity: String
    var recipe by Delegates.notNull<Long>()
}