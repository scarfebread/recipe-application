package thecookingpot.client

import net.minidev.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

class HttpClient {
    fun <T> post(
        endpoint: String,
        responseType: Class<T>,
        values: JSONObject,
        accessToken: String
    ): ResponseEntity<T> {
        val headers = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
            add("Content-Type", "application/json")
            add("Accept", "application/json")
        }

        val restTemplate = RestTemplate()
        val request = HttpEntity(values, headers)

        return restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            request,
            responseType)
    }
}