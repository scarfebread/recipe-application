package thecookingpot.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import org.springframework.stereotype.Component
import thecookingpot.oauth.service.OAuthIntegrationService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuthLogoutHandler @Autowired constructor(
    private val oAuthService: OAuthIntegrationService): SimpleUrlLogoutSuccessHandler(), LogoutSuccessHandler {

    override fun onLogoutSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        (authentication.principal as RecipeUserDetails).user.let { user ->
            if (user.oAuthUser) {
                oAuthService.logout(user)
            }
        }

        super.onLogoutSuccess(request, response, authentication)
    }
}