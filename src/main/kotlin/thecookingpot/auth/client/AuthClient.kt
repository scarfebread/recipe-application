package thecookingpot.auth.client

import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import thecookingpot.recipe.exception.AuthServerException

@Service
class AuthClient {
    @Value("\${spring.security.oauth2.client.registration.recipe-auth.client-secret}")
    private lateinit var clientId: String

    @Value("\${spring.security.oauth2.client.registration.recipe-auth.client-secret}")
    private lateinit var clientSecret: String

    private val httpClient: HttpClient = HttpClient()

    fun changePassword(username: String, password: String, accessToken: String) {
        val response = try {
            httpClient.post(
                "http://localhost:8085/api/change-password",
                String::class.java,
                JSONObject().apply {
                    put("username", username)
                    put("password", password)
                    put("accessToken", accessToken)
                },
                clientId,
                clientSecret
            )
        } catch (e: Exception) {
            println(e.message)
            throw AuthServerException("Unable to contact the login server")
        }

        if (response.statusCodeValue >= 300) {
            throw AuthServerException(response.body as String)
        }
    }

    fun revoke(accessToken: String) {
        val response = try {
            httpClient.post(
                "http://localhost:8085/revoke",
                String::class.java,
                JSONObject().apply {
                    put("token", accessToken)
                    put("token_type_hint", "access_token")
                },
                clientId,
                clientSecret
            )
        } catch (e: Exception) {
            println(e.message)
            throw AuthServerException("Unable to contact the login server")
        }

        if (response.statusCodeValue >= 300) {
            throw AuthServerException(response.body as String)
        }
    }
}