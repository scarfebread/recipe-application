package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.User
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}