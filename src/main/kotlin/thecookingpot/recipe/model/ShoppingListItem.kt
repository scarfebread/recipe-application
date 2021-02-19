package thecookingpot.recipe.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "shopping_list")
class ShoppingListItem : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToOne
    lateinit var user: User

    @OneToOne(cascade = [CascadeType.PERSIST])
    lateinit var ingredient: Ingredient
}