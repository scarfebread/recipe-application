package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.auth.client.AuthClient
import thecookingpot.recipe.dto.PasswordResetDto
import thecookingpot.recipe.exception.InvalidPasswordTokenException
import thecookingpot.recipe.exception.UserNotFoundException
import thecookingpot.recipe.model.PasswordResetToken
import thecookingpot.recipe.model.User
import thecookingpot.recipe.repository.PasswordTokenRepository
import thecookingpot.recipe.repository.UserRepository
import thecookingpot.auth.security.RecipeUserDetails
import thecookingpot.auth.service.AuthService
import java.util.*
import kotlin.jvm.Throws

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordTokenRepository: PasswordTokenRepository,
    private val emailService: EmailService,
    private val authService: AuthService,
    private val authClient: AuthClient
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
//        authService.authenticateUser(recipeUserDetails, Role.CHANGE_PASSWORD)
    }

    fun changePassword(user: User, password: String) {
        authClient.changePassword(
            user.username,
            password,
            authService.getAccessToken()
        )
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

    fun createUser(username: String, email: String): User {
        return userRepository.save(User().apply {
            this.username = username
            password = ""
            this.email = email
            newUser = true
        })
    }
}