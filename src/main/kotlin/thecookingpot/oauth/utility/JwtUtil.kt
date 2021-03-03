package thecookingpot.oauth.utility

import com.beust.klaxon.Klaxon
import thecookingpot.oauth.model.IdToken
import java.util.*

fun decodeJwt(token: String): IdToken {
    return token.split(".")[1].let {
        Klaxon().parse<IdToken>(
            Base64.getDecoder().decode(it).toString(Charsets.UTF_8)
        )!!
    }
}