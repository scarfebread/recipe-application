package recipeapplication.dto

import javax.validation.constraints.NotEmpty
import kotlin.properties.Delegates

class InventoryItemDto {
    var id by Delegates.notNull<Long>()
    lateinit var ingredient: @NotEmpty String
    lateinit var quantity: String
}