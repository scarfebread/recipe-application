package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import recipeapplication.model.PasswordResetToken
import recipeapplication.model.User
import java.util.*
import javax.transaction.Transactional

interface PasswordTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findByToken(token: String): Optional<PasswordResetToken>

    @Transactional
    fun deleteByUser(user: User)
}