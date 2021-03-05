package thecookingpot.recipe.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var username: String
    lateinit var password: String
    lateinit var email: String
    var newUser = true
    var oAuthUser = false
}