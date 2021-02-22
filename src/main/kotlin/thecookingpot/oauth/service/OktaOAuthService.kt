package thecookingpot.oauth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import thecookingpot.oauth.client.OAuthClient
import thecookingpot.oauth.config.OktaProperties

@Service
class OktaOAuthService @Autowired constructor(private val oAuthClient: OAuthClient, private val properties: OktaProperties) {
    fun processOktaAuthorisationCode(code: String, state: String) {
        oAuthClient.processAuthorisationCode(
                properties.clientId,
                properties.clientSecret,
                properties.redirectUri,
                properties.tokenEndpoint,
                code,
                state
        )
    }
}