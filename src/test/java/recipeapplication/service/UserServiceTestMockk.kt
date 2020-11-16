package recipeapplication.service

import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.springframework.security.crypto.password.PasswordEncoder
import recipeapplication.dto.UserDto
import recipeapplication.exception.UserNotFoundException
import recipeapplication.model.PasswordResetToken
import recipeapplication.model.User
import recipeapplication.repository.PasswordTokenRepository
import recipeapplication.repository.UserRepository
import java.util.*
import kotlin.test.assertNotNull

class UserServiceTestMockk {
    companion object {
        private const val INVALID_USERNAME = "INVALID_USERNAME"
        private const val INVALID_EMAIL = "INVALID_EMAIL"
        private const val VALID_EMAIL = "VALID_EMAIL"
        private const val VALID_USERNAME = "VALID_USERNAME"
        private const val SERVER_NAME = "SERVER_NAME"
        private const val VALID_TOKEN = "VALID_TOKEN"
        private const val INVALID_TOKEN = "INVALID_TOKEN"
        private const val EXPIRED_TOKEN = "EXPIRED_TOKEN"
        private const val PASSWORD = "PASSWORD"
        private const val ENCODED_PASSWORD = "ENCODED_PASSWORD"
    }

    private lateinit var userRepository: UserRepository
    private lateinit var passwordTokenRepository: PasswordTokenRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var emailService: EmailService

    @Before
    fun setup() {
        userRepository = mockk()
        passwordTokenRepository = mockk()
        emailService = mockk(relaxed = true)
        passwordEncoder = mockk()
        authService = mockk()

        every { userRepository.findByUsername(INVALID_USERNAME) } returns Optional.empty()
        every { userRepository.findByEmail(INVALID_EMAIL) } returns Optional.empty()
        every { userRepository.findByUsername(VALID_USERNAME) } returns Optional.of(User())
        every { userRepository.findByEmail(VALID_EMAIL) } returns Optional.of(User())

        userService = UserService(userRepository, passwordTokenRepository, emailService, passwordEncoder, authService)
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when no username or email in request`() {
        userService.createPasswordResetToken("", UserDto())
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when username does not exist`() {
        userService.createPasswordResetToken(
                SERVER_NAME,
                UserDto().apply { username = INVALID_USERNAME }
        )
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when email does not exist`() {
        userService.createPasswordResetToken(
                SERVER_NAME,
                UserDto().apply { email = INVALID_EMAIL }
        )
    }

    @Test
    fun `Should create password reset token for valid username`() {
        val userDto = UserDto().apply { username = VALID_USERNAME }

        every { passwordTokenRepository.save(any()) } returns PasswordResetToken()

        userService.createPasswordResetToken(SERVER_NAME, userDto)

        val savedToken = slot<PasswordResetToken>()

        verify { emailService.sendPasswordReset(any(), any(), SERVER_NAME) }
        verify { passwordTokenRepository.save(capture(savedToken)) }

        assertNotNull(savedToken.captured.user)
        assertNotNull(savedToken.captured.token)
    }
}