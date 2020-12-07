package recipeapplication.model

import com.fasterxml.jackson.annotation.JsonInclude
import recipeapplication.utility.toImperial
import recipeapplication.utility.toMetric
import javax.persistence.*

@Entity
@Table(name = "ingredients")
class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var description: String
    lateinit var metric: String
    lateinit var imperial: String

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    var isInShoppingList = false

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    var inventoryItems: List<Ingredient>? = null

    @OneToOne
    var user: User? = null

    constructor(description: String, quantity: String, user: User) {
        this.description = description
        metric = toMetric(quantity)
        imperial = toImperial(quantity)
        this.user = user
    }

    constructor()
}