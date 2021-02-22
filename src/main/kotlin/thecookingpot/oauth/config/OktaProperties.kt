package thecookingpot.oauth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class OktaProperties {
    @Value("\${oauth.okta.client_id}")
    lateinit var clientId: String;

    @Value("\${oauth.okta.client_secret}")
    lateinit var clientSecret: String

    @Value("\${oauth.okta.redirect_uri}")
    lateinit var redirectUri: String

    @Value("\${oauth.okta.token_endpoint}")
    lateinit var tokenEndpoint: String

    @Value("\${oauth.okta.auth_endpoint}")
    lateinit var authEndpoint: String

    @Value("\${oauth.okta.pkce_challenge_method}")
    lateinit var pkceChallengeMethod: String
}