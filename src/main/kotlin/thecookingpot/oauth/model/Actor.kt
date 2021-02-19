package thecookingpot.oauth.model

import thecookingpot.oauth.utility.generatePkceCode
import thecookingpot.oauth.utility.generateString

class Actor {
    val pkceCodeVerifier: String
    val pkceCodeChallenge: String
    val state: String

    init {
        generatePkceCode().also { pkceCode ->
            pkceCodeVerifier = pkceCode.code
            pkceCodeChallenge = pkceCode.challenge
        }

        state = generateString(20)
    }
}