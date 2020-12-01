package recipeapplication.dto

import kotlin.properties.Delegates

class DeleteStepDto {
    var stepId by Delegates.notNull<Long>()
    var recipeId by Delegates.notNull<Long>()
}