package thecookingpot.recipe.dto

import javax.validation.constraints.NotEmpty
import kotlin.properties.Delegates

class ShoppingListItemDto {
    var id by Delegates.notNull<Long>()
    lateinit var ingredient: @NotEmpty String
    lateinit var quantity: String
}