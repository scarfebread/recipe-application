package thecookingpot.oauth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import thecookingpot.oauth.config.OktaProperties
import thecookingpot.oauth.exception.OAuthException
import thecookingpot.oauth.repository.ActorRepository
import thecookingpot.oauth.service.OktaOAuthService

@Controller
@RequestMapping(path = ["/auth/"])
class AuthWebController
@Autowired constructor(
        private val authService: OktaOAuthService,
        private val actorRepository: ActorRepository,
        private val oktaProperties: OktaProperties) {
    @GetMapping("/okta-login")
    fun loginWithOkta(): String {
        val actor = actorRepository.createActor();

        // TODO don't do the redirect like this
        return "redirect:${oktaProperties.authEndpoint}?" +
                "response_type=code&" +
                "scope=customer_account&" +
                "client_id=${oktaProperties.clientId}&" +
                "state=${actor.state}&" +
                "redirect_uri=${oktaProperties.redirectUri}&" +
                "code_challenge=${actor.pkceCodeChallenge}&" +
                "code_challenge_method=${oktaProperties.pkceChallengeMethod}"
    }

    @GetMapping("/okta-redirect")
    fun oktaLoginRedirect(@RequestParam code: String, @RequestParam state: String): String {
        return try {
            authService.processOktaAuthorisationCode(code, state)
            "home.html"
        } catch (e: OAuthException) {
            "login.html"
        }
    }
}