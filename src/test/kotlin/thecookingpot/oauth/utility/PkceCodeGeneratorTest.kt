package thecookingpot.oauth.utility

import org.junit.Test
import kotlin.test.assertEquals

class PkceCodeGeneratorTest {
    @Test
    fun `should hash value correctly`() {
        val code = "mTSFwWnk_Z--_Br6X0jyVVaE3F9TdgqJTibZNmRmEC.jPm04heS12k5GTujMNHvEJCOCTjPMYish.iz4.7JwjGWPJLF05ckp8I~FEYZckvnObv4VB2KMAEGD1iQj280w"
        val expectedHash = "CwF6jaOekboLWQDy0cKk50IoDmcx7lKhaECxeTt4c0c"

        assertEquals(expectedHash, hashCode(code))
    }
}