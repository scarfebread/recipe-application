package recipeapplication.model

import javax.persistence.*

@Entity
@Table(name = "steps")
class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var description: String

    constructor()
    constructor(description: String) {
        this.description = description
    }
}