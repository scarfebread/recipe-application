package thecookingpot.auth.client

import net.minidev.json.JSONObject
import org.apache.tomcat.jni.User.username
import org.apache.tomcat.util.codec.binary.Base64.encodeBase64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate


class HttpClient {
    fun <T> post(
        endpoint: String,
        responseType: Class<T>,
        values: JSONObject,
        clientId: String,
        clientSecret: String
    ): ResponseEntity<T> {
        val headers = HttpHeaders().apply {
            add("Authorization", getAuthHeader(clientId, clientSecret))
            add("Content-Type", "application/json")
            add("Accept", "application/json")
        }

        // TODO better way to do this with the headers
        val restTemplate = RestTemplate()
        val request = HttpEntity(values, headers)

        return restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            request,
            responseType)
    }

    private fun getAuthHeader(clientId: String, clientSecret: String): String {
        val encodedAuth = encodeBase64(
            "$clientId:$clientSecret".toByteArray()
        )
        return "Basic " + String(encodedAuth)
    }
}