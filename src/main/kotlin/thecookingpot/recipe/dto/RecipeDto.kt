package thecookingpot.recipe.dto

import kotlin.properties.Delegates

class RecipeDto {
    var id : Long? = null
    lateinit var notes: String
    var rating by Delegates.notNull<Long>()
    var serves by Delegates.notNull<Long>()
    lateinit var cookTime: String
    lateinit var prepTime: String
    lateinit var difficulty: String
    var newUser : String? = null
}