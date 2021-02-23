package thecookingpot.oauth.model

class Token (
    private val expiresIn: String,
    val accessToken: String,
    val refreshToken: String,
    val scope: String) {

    fun isValid(): Boolean {
        // TODO
        return true
    }
}