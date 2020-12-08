package recipeapplication.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "recipes")
class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var title: String
    var sharedBy: String? = null
    var rating: Long? = null
    var notes: String? = null
    var serves: Long? = null
    lateinit var cookTime: String
    lateinit var prepTime: String
    lateinit var totalTime: String
    lateinit var difficulty: String

    @OneToOne
    lateinit var user: User

    @JoinTable(name = "recipe_ingredients", joinColumns = [JoinColumn(name = "recipe", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "ingredient", referencedColumnName = "id")])
    @ManyToMany(cascade = [CascadeType.ALL])
    var ingredients = mutableListOf<Ingredient>()

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "recipe")
    var steps = mutableListOf<Step>()

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
    private val recentlyViewed: List<RecentlyViewed>? = null // TODO is this needed?

    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
    }

    fun addStep(step: Step) {
        steps.add(step)
    }
}