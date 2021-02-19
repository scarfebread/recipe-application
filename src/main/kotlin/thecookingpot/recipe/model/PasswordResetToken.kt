package thecookingpot.recipe.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "passwordResetTokens")
class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
    private lateinit var expiryDate: Date
    lateinit var token: String

    @OneToOne
    lateinit var user: User

    constructor()
    constructor(token: String, user: User) {
        this.token = token
        this.user = user
        expiryDate = dateAfter24Hours()
    }

    private fun dateAfter24Hours() : Date {
        return Calendar.getInstance().apply {
            time = Date()
            add(Calendar.DATE, ONE_DAY)
        }.time
    }

    val isExpired: Boolean
        get() = Date() >= expiryDate

    fun setExpiryDate(expiryDate: Date) {
        this.expiryDate = expiryDate
    }

    companion object {
        private const val ONE_DAY = 1
    }
}