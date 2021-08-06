package thecookingpot.dto

import javax.validation.constraints.NotEmpty

class CreateRecipeDto {
    lateinit var title: @NotEmpty String
}