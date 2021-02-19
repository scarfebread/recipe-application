package thecookingpot.recipe.dto

import javax.validation.constraints.NotEmpty
import kotlin.properties.Delegates

class CreateStepDto {
    lateinit var description: @NotEmpty String
    var recipe by Delegates.notNull<Long>()
}