package thecookingpot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import thecookingpot.model.User
import thecookingpot.security.AuthenticationPrincipal


@Service
class AuthService @Autowired constructor(private val clientService: OAuth2AuthorizedClientService) {
    val loggedInUser: User
        get() = (getContext().authentication.principal as AuthenticationPrincipal).user

    fun getAccessToken(): String {
        val authToken = getContext().authentication as OAuth2AuthenticationToken

        val client: OAuth2AuthorizedClient = clientService.loadAuthorizedClient(
            authToken.authorizedClientRegistrationId,
            authToken.name
        )

        return client.accessToken.tokenValue
    }

    fun disablePasswordReset() {
        val user = getContext().authentication.principal as AuthenticationPrincipal
//        if (user.changePasswordAccess) {
//            user.changePasswordAccess = false
//            getContext().authentication = UsernamePasswordAuthenticationToken(user, null)
//        }
    }

    fun disableUserSession() {
        SecurityContextHolder.clearContext()
    }
}