package thecookingpot.client

import net.minidev.json.JSONObject
import org.springframework.stereotype.Service
import thecookingpot.exception.AuthServerException

@Service
class ChangePasswordClient {
    private val httpClient: HttpClient = HttpClient()

    fun changePassword(username: String, password: String, accessToken: String) {
        val response = try {
            httpClient.post(
                "http://localhost:8085/api/change-password",
                String::class.java,
                JSONObject().apply {
                    put("username", username)
                    put("password", password)
                },
                accessToken
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