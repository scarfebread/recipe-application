package thecookingpot.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.model.User
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}