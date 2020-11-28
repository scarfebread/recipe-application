package recipeapplication.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class CreateRecipeDto {
    lateinit var title: @NotNull @NotEmpty String
}