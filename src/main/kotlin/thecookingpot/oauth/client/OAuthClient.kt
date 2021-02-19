package thecookingpot.oauth.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import thecookingpot.oauth.exception.OAuthException

@Service
class OAuthClient {
    fun processAuthorisationCode(
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            tokenEndpoint: String,
            authorisationCode: String,
            state: String
    ) {
        val headers = HttpHeaders().apply {
            add("Content-Type", "application/x-www-form-urlencoded")
        }

        val tokenRequest = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("redirect_uri", redirectUri)
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code_verifier", "8f8b176f5dbec3bbd9e020759f012233528a86dc757837107a9691aa")
            add("code", authorisationCode)
        }

        val restTemplate = RestTemplate()
        val request = HttpEntity(tokenRequest, headers)

        val response = try {
            val response = restTemplate.exchange(
                    tokenEndpoint,
                    HttpMethod.POST,
                    request,
                    TokenResponse::class.java)
            response.body
        } catch (e: HttpStatusCodeException) {
            throw OAuthException(e)
        }

        // TODO process the access token
        println(response.access_token)
    }
}