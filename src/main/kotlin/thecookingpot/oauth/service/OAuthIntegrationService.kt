package thecookingpot.oauth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.oauth.model.IdToken
import thecookingpot.oauth.repository.ActorRepository
import thecookingpot.oauth.utility.generateString
import thecookingpot.recipe.dto.UserDto
import thecookingpot.recipe.model.User
import thecookingpot.security.service.AuthService
import thecookingpot.recipe.service.SignupService
import thecookingpot.recipe.service.UserService
import thecookingpot.security.RecipeUserDetails
import thecookingpot.security.Role

@Service
class OAuthIntegrationService @Autowired constructor(
    private val userService: UserService,
    private val authService: AuthService,
    private val signupService: SignupService,
    private val actorRepository: ActorRepository,
    private val oAuthService: OktaOAuthService
) {
    fun login(idToken: IdToken) {
        val existingUser = userService.getUserByEmail(idToken.email)

        existingUser?.apply {
            authService.authenticateUser(RecipeUserDetails(existingUser), Role.USER)
        } ?: let {
            signupService.registerNewUser(
                UserDto().apply {
                    username = idToken.email
                    email = idToken.email
                    password = generateString(10)
                },
                true
            )
        }
    }

    fun logout(user: User) {

    }
}