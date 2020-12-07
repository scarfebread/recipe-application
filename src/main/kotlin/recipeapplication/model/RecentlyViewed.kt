package recipeapplication.model

import javax.persistence.*

@Entity
@Table(name = "recentlyViewed")
class RecentlyViewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @ManyToOne
    @JoinColumn(name = "recipe")
    lateinit var recipe: Recipe

    @OneToOne
    lateinit var user: User
}