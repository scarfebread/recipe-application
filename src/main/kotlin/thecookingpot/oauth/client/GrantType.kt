package thecookingpot.oauth.client

enum class GrantType(private val parameterName: String) {
    AUTH_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    override fun toString(): String {
        return parameterName
    }
}