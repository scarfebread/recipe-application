package recipeapplication.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import recipeapplication.dto.PasswordResetDto
import recipeapplication.exception.InvalidPasswordTokenException
import recipeapplication.exception.UserNotFoundException
import recipeapplication.model.PasswordResetToken
import recipeapplication.model.User
import recipeapplication.repository.PasswordTokenRepository
import recipeapplication.repository.UserRepository
import recipeapplication.security.RecipeUserDetails
import recipeapplication.security.Role
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
        }

        if (!result.isPresent) {
            throw UserNotFoundException()
        }

        val token = UUID.randomUUID().toString()
        val passwordResetToken = PasswordResetToken(token, result.get())
        passwordTokenRepository.save(passwordResetToken)
        emailService.sendPasswordReset(result.get(), token, serverName)
    }

    @Throws(InvalidPasswordTokenException::class)
    fun processPasswordResetToken(token: String) {
        val passwordResetToken = passwordTokenRepository.findByToken(token)

        if (!passwordResetToken.isPresent || passwordResetToken.get().isExpired) {
            throw InvalidPasswordTokenException()
        }

        val user = passwordResetToken.get().user
        val recipeUserDetails = RecipeUserDetails(user)
        recipeUserDetails.changePasswordAccess = true
        authService.authenticateUser(recipeUserDetails, Role.CHANGE_PASSWORD)
    }

    fun changePassword(user: User, password: String) {
        user.password = passwordEncoder.encode(password)
        userRepository.save(user)
        passwordTokenRepository.deleteByUser(user)
        authService.disablePasswordReset()
    }

    @Throws(UserNotFoundException::class) // TODO can I get rid of these when the client is Kotlin?
    fun getUser(username: String): User {
        val user = userRepository.findByUsername(username)
        if (!user.isPresent) {
            throw UserNotFoundException()
        }
        return user.get()
    }

    fun deleteAccount() {
        userRepository.delete(
                authService.loggedInUser
        )
    }

    fun turnOffInstructions() {
        val user = authService.loggedInUser
        if (user.newUser) {
            user.newUser = false
            userRepository.save(user)
        }
    }

}