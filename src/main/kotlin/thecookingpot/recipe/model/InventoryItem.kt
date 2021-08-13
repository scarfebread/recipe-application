package thecookingpot.recipe.model

import javax.persistence.*

@Entity
@Table(name = "inventory")
class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToOne
    lateinit var user: User

    @OneToOne(cascade = [CascadeType.PERSIST])
    lateinit var ingredient: Ingredient

    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id", insertable = false, updatable = false)
    @OneToOne
    val shoppingListItem: ShoppingListItem? = null // TODO is this needed?
}