package thecookingpot.oauth.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import thecookingpot.oauth.exception.OAuthException

// TODO bean?
class HttpClient {
    fun <T> formEncodedPost(
        endpoint: String,
        values: LinkedMultiValueMap<String, String>,
        responseType: Class<T>,
    ): T {
        val headers = HttpHeaders().apply {
            add("Content-Type", "application/x-www-form-urlencoded")
        }

        val restTemplate = RestTemplate()
        val request = HttpEntity(values, headers)

        val response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            request,
            responseType)
        return response.body
    }
}