package thecookingpot.oauth.client

class TokenResponse {
    // TODO remove underscores
    lateinit var token_type: String
    lateinit var expires_in: String
    lateinit var access_token: String
    lateinit var refresh_token: String
    lateinit var scope: String
}