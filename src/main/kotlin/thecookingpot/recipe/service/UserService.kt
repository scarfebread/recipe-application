package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import thecookingpot.recipe.dto.PasswordResetDto
import thecookingpot.recipe.exception.InvalidPasswordTokenException
import thecookingpot.recipe.exception.UserNotFoundException
import thecookingpot.recipe.model.PasswordResetToken
import thecookingpot.recipe.model.User
import thecookingpot.recipe.repository.PasswordTokenRepository
import thecookingpot.recipe.repository.UserRepository
import thecookingpot.security.RecipeUserDetails
import thecookingpot.security.Role
import thecookingpot.security.service.AuthService
import java.util.*
import kotlin.jvm.Throws

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordTokenRepository: PasswordTokenRepository,
        private val emailService: EmailService,
        private val passwordEncoder: PasswordEncoder,
        private val authService: AuthService
) {
    @Throws(UserNotFoundException::class)
    fun createPasswordResetToken(serverName: String, passwordResetDto: PasswordResetDto) {
        val result = when {
            passwordResetDto.username != null -> userRepository.findByUsername(passwordResetDto.username!!)
            passwordResetDto.email != null -> userRepository.findByEmail(passwordResetDto.email!!)
            else -> throw UserNotFoundException()
        } ?: throw UserNotFoundException()

        val token = UUID.randomUUID().toString()
        val passwordResetToken = PasswordResetToken(token, result)
        passwordTokenRepository.save(passwordResetToken)
        emailService.sendPasswordReset(result, token, serverName)
    }

    @Throws(InvalidPasswordTokenException::class)
    fun processPasswordResetToken(token: String) {
        val passwordResetToken = passwordTokenRepository.findByToken(token)

        if (!passwordResetToken.isPresent || passwordResetToken.get().isExpired) {
            throw InvalidPasswordTokenException()
        }

        val user = passwordResetToken.get().user
        val recipeUserDetails = RecipeUserDetails(user).apply {
            changePasswordAccess = true
        }
        authService.authenticateUser(recipeUserDetails, Role.CHANGE_PASSWORD)
    }

    fun changePassword(user: User, password: String) {
        user.password = passwordEncoder.encode(password)
        userRepository.save(user)
        passwordTokenRepository.deleteByUser(user)
        authService.disablePasswordReset()
    }

    @Throws(UserNotFoundException::class) // TODO can I get rid of these when the unit tests are Kotlin?
    fun getUser(username: String): User {
        return userRepository.findByUsername(username) ?: throw UserNotFoundException()
    }

    fun deleteAccount() {
        userRepository.delete(
            authService.loggedInUser
        )
    }

    fun turnOffInstructions() {
        authService.loggedInUser.run {
            if (newUser) {
                newUser = false
                userRepository.save(this)
            }
        }

    }

    fun getUserByEmail(emailAddress: String): User? {
        return userRepository.findByEmail(emailAddress)
    }
}