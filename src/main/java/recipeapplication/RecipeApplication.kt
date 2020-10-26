package recipeapplication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RecipeApplication

fun main(args: Array<String>) {
    runApplication<RecipeApplication>(*args)
}