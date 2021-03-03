package thecookingpot.recipe.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.springframework.security.crypto.password.PasswordEncoder
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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserServiceTest {
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
        userRepository = mockk(relaxed = true)
        passwordTokenRepository = mockk(relaxed = true)
        emailService = mockk(relaxed = true)
        passwordEncoder = mockk()
        authService = mockk(relaxed = true)

        every { userRepository.findByUsername(INVALID_USERNAME) } returns null
        every { userRepository.findByEmail(INVALID_EMAIL) } returns null
        every { userRepository.findByUsername(VALID_USERNAME) } returns User()
        every { userRepository.findByEmail(VALID_EMAIL) } returns User()

        val user = User().apply { username = VALID_USERNAME }
        val oneHourAgo = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
        val oneHourInTheFuture = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }

        val validPasswordResetToken = PasswordResetToken().apply {
            setExpiryDate(oneHourInTheFuture.time)
            this.user = user
        }

        val expiredPasswordResetToken = PasswordResetToken().apply {
            setExpiryDate(oneHourAgo.time)
        }

        every { passwordTokenRepository.findByToken(INVALID_TOKEN) } returns Optional.empty()
        every { passwordTokenRepository.findByToken(VALID_TOKEN) } returns Optional.of(validPasswordResetToken)
        every { passwordTokenRepository.findByToken(EXPIRED_TOKEN) } returns Optional.of(expiredPasswordResetToken)

        every { passwordEncoder.encode(PASSWORD) } returns ENCODED_PASSWORD

        userService = UserService(userRepository, passwordTokenRepository, emailService, passwordEncoder, authService)
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when no username or email in request`() {
        userService.createPasswordResetToken("", PasswordResetDto())
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when username does not exist`() {
        userService.createPasswordResetToken(
                SERVER_NAME,
                PasswordResetDto().apply { username = INVALID_USERNAME }
        )
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when email does not exist`() {
        userService.createPasswordResetToken(
                SERVER_NAME,
                PasswordResetDto().apply { email = INVALID_EMAIL }
        )
    }

    @Test
    fun `Should create password reset token for valid username`() {
        val passwordResetDto = PasswordResetDto().apply { username = VALID_USERNAME }

        every { passwordTokenRepository.save(any()) } returns PasswordResetToken()

        userService.createPasswordResetToken(SERVER_NAME, passwordResetDto)

        val savedToken = slot<PasswordResetToken>()

        verify { emailService.sendPasswordReset(any(), any(), SERVER_NAME) }
        verify { passwordTokenRepository.save(capture(savedToken)) }

        assertNotNull(savedToken.captured.user)
        assertNotNull(savedToken.captured.token)
    }

    @Test
    fun `Should create password reset token for valid email address`() {
        val passwordResetDto = PasswordResetDto().apply { email = VALID_EMAIL }

        every { passwordTokenRepository.save(any()) } returns PasswordResetToken()

        userService.createPasswordResetToken(SERVER_NAME, passwordResetDto)

        val savedToken = slot<PasswordResetToken>()

        verify { emailService.sendPasswordReset(any(), any(), SERVER_NAME) }
        verify { passwordTokenRepository.save(capture(savedToken)) }

        assertNotNull(savedToken.captured.user)
        assertNotNull(savedToken.captured.token)
    }

    @Test(expected = InvalidPasswordTokenException::class)
    fun `Should throw invalid password token exception when token doesn't exist`() {
        userService.processPasswordResetToken(INVALID_TOKEN)
    }

    @Test(expected = InvalidPasswordTokenException::class)
    fun `Should throw invalid password token exception when token has expired`() {
        userService.processPasswordResetToken(EXPIRED_TOKEN)
    }

    @Test
    fun `Should enable password reset for valid token`() {
        userService.processPasswordResetToken(VALID_TOKEN)

        val processedUserDetails = slot<RecipeUserDetails>()

        verify { authService.authenticateUser(capture(processedUserDetails), Role.CHANGE_PASSWORD) }

        assertEquals(VALID_USERNAME, processedUserDetails.captured.username)
        assertTrue(processedUserDetails.captured.changePasswordAccess)
    }

    @Test
    fun `Should change password successfully`() {
        val user = User()

        every { userRepository.save(user) } returns user

        userService.changePassword(user, PASSWORD)

        val savedUser = slot<User>()

        verify { userRepository.save(capture(savedUser)) }
        verify { passwordTokenRepository.deleteByUser(user) }
        verify { authService.disablePasswordReset() }

        assertEquals(ENCODED_PASSWORD, savedUser.captured.password)
    }

    @Test(expected = UserNotFoundException::class)
    fun `Should throw UserNotFoundException when user does not exist`() {
        userService.getUser(INVALID_USERNAME)
    }

    @Test
    fun `Should get user when valid username provided`() {
        val user = User()

        every { userRepository.findByUsername(VALID_USERNAME) } returns user

        assertEquals(user, userService.getUser(VALID_USERNAME))
    }

    @Test
    fun `Should delete account successfully`() {
        val user = User()

        every { authService.loggedInUser } returns user

        userService.deleteAccount()

        verify { userRepository.delete(user) }
    }

    @Test
    fun `Should turn off instructions if the user is new`() {
        val user = User().apply { newUser = true }

        every { authService.loggedInUser } returns user
        every { userRepository.save(user) } returns user

        userService.turnOffInstructions()

        val submittedUser = slot<User>()

        verify { userRepository.save(capture(submittedUser)) }

        assertFalse(submittedUser.captured.newUser)
    }

    @Test
    fun `Should not turn off instructions if the user is new`() {
        val user = User().apply { newUser = false }

        every { authService.loggedInUser } returns user

        userService.turnOffInstructions()

        verify(exactly = 0) { userRepository.save(user) }
    }
}