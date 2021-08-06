package thecookingpot.dto

import javax.validation.constraints.NotEmpty
import kotlin.properties.Delegates

class UpdateStepDto {
    var id by Delegates.notNull<Long>()
    lateinit var description: @NotEmpty String
    var recipe by Delegates.notNull<Long>()
}