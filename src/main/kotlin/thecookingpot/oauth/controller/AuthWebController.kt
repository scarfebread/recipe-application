package thecookingpot.oauth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import thecookingpot.oauth.repository.ActorRepository
import thecookingpot.oauth.service.OktaOAuthService

@Controller
@RequestMapping(path = ["/auth/"])
class AuthWebController @Autowired constructor(private val authService: OktaOAuthService, private val actorRepository: ActorRepository) {
    @GetMapping("/okta-login")
    fun loginWithOkta(): String {
        val actor = actorRepository.createActor();

        // TODO extract these to @properties class
        // TODO don't do the redirect like this
        return "redirect:https://dev-25996170.okta.com/oauth2/default/v1/authorize?" +
                "response_type=code&" +
                "scope=customer_account&" +
                "client_id=0oa7e1anmiOgPajIx5d6&" +
                "state=${actor.state}&" +
                "redirect_uri=http://localhost:8080/auth/okta-redirect&" +
                "code_challenge=${actor.pkceCodeChallenge}&" +
                "code_challenge_method=S256"
    }

    @GetMapping("/okta-redirect")
    fun oktaLoginRedirect(@RequestParam code: String, @RequestParam state: String): String {
        authService.processOktaAuthorisationCode(code, state)
        return "home.html"
    }
}