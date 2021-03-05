package thecookingpot.oauth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.oauth.client.OAuthClient
import thecookingpot.oauth.config.OktaProperties
import thecookingpot.oauth.model.Token
import thecookingpot.oauth.repository.ActorRepository
import thecookingpot.oauth.utility.decodeJwt

@Service
class OktaOAuthService @Autowired constructor(
    private val oAuthClient: OAuthClient,
    private val properties: OktaProperties,
    private val actorRepository: ActorRepository,
    private val oAuthIntegrationService: OAuthIntegrationService) {

    // TODO naming
    fun processOktaAuthorisationCode(code: String, state: String) {
        val actor = actorRepository.findActorByState(state);
        val tokenResponse = oAuthClient.processAuthorisationCode(
            properties.clientId,
            properties.clientSecret,
            properties.redirectUri,
            properties.tokenEndpoint,
            actor,
            code
        )

        actor.token = Token(
            tokenResponse.expires_in,
            tokenResponse.access_token,
            tokenResponse.refresh_token,
            tokenResponse.scope,
            decodeJwt(tokenResponse.id_token)
        )

        oAuthIntegrationService.login(actor.token.idToken)
    }
}