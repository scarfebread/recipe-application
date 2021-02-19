package thecookingpot.oauth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import thecookingpot.oauth.client.OAuthClient

@Value("\${oauth.okta.client_id}")
lateinit var clientId: String;

@Value("\${oauth.okta.client_secret}")
private lateinit var clientSecret: String

@Value("\${oauth.okta.redirect_uri}")
private lateinit var redirectUri: String

@Value("\${oauth.okta.token_endpoint}")
private lateinit var tokenEndpoint: String

@Service
class OktaOAuthService @Autowired constructor(private val oAuthClient: OAuthClient) {
    fun processOktaAuthorisationCode(code: String, state: String) {
        oAuthClient.processAuthorisationCode(
                clientId,
                clientSecret,
                redirectUri,
                tokenEndpoint,
                code,
                state
        )
    }
}