package thecookingpot.oauth.client

import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import thecookingpot.oauth.exception.OAuthException
import thecookingpot.oauth.model.Actor
import thecookingpot.oauth.model.IdToken
import thecookingpot.oauth.utility.decodeJwt

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
    ): IdToken {
        val tokenRequest = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", GrantType.AUTH_CODE.toString())
            add("redirect_uri", redirectUri)
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code_verifier", actor.pkceCodeVerifier)
            add("code", authorisationCode)
        }

        val response = try {
            httpClient.formEncodedPost(
                tokenEndpoint,
                tokenRequest,
                TokenResponse::class.java
            )
        } catch (e: HttpStatusCodeException) {
            // TODO this is being swallowed somehow
            throw OAuthException(e)
        }

        return decodeJwt(response.id_token)
    }

    fun processRefreshToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        tokenEndpoint: String,
        actor: Actor,
    ) {
        val tokenRequest = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", GrantType.REFRESH_TOKEN.toString())
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code", actor.token.refreshToken)
        }

        val response = try {
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