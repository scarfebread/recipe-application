package thecookingpot.oauth.client

import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import thecookingpot.oauth.exception.OAuthException
import thecookingpot.oauth.model.Actor

@Service
class OAuthClient {
    private val httpClient: HttpClient = HttpClient()

    fun processAuthorisationCode(
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            tokenEndpoint: String,
            actor: Actor,
            authorisationCode: String,
    ) {
        val response = callTokenEndpoint(
            clientId,
            clientSecret,
            redirectUri,
            tokenEndpoint,
            actor,
            GrantType.AUTH_CODE,
            authorisationCode
        )

        // TODO process this
        println(response.access_token)
    }

    fun processRefreshToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        tokenEndpoint: String,
        actor: Actor,
    ) {
        val response = callTokenEndpoint(
            clientId,
            clientSecret,
            redirectUri,
            tokenEndpoint,
            actor,
            GrantType.REFRESH_TOKEN,
            actor.token.refreshToken
        )
    }

    fun callTokenEndpoint(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        tokenEndpoint: String,
        actor: Actor,
        grantType: GrantType,
        code: String,
    ): TokenResponse {
        val tokenRequest = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", grantType.toString())
            add("redirect_uri", redirectUri)
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code_verifier", actor.pkceCodeVerifier)
            add("code", code)
        }

        return try {
            httpClient.formEncodedPost(
                tokenEndpoint,
                tokenRequest,
                TokenResponse::class.java
            )
        } catch (e: HttpStatusCodeException) {
            // TODO this is being swallowed somehow
            throw OAuthException(e)
        }
    }
}