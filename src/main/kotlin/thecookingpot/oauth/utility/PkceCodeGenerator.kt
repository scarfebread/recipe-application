package thecookingpot.oauth.utility

import thecookingpot.oauth.model.PkceCode
import java.security.MessageDigest
import java.util.*

private const val PKCE_CODE_LENGTH = 128

fun generatePkceCode(): PkceCode {
    generateString(PKCE_CODE_LENGTH).also { code ->
        return PkceCode(code, hashCode(code))
    }
}

private fun hashCode(code: String): String {
    MessageDigest.getInstance("SHA-256").also { messageDigest ->
        return Base64.getUrlEncoder().encodeToString(
                messageDigest.digest(code.toByteArray(Charsets.UTF_8))
        )
    }
}