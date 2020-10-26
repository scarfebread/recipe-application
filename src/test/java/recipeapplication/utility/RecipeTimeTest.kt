package recipeapplication.utility

import junit.framework.TestCase.assertEquals
import org.junit.Test

class RecipeTimeTest {
    @Test
    fun `Should combine valid cook and prep times`() {
        assertEquals("21:52", combineCookAndPrepTime("10:21", "11:31"))
    }

    @Test
    fun `Should combine large cook and prep times`() {
        assertEquals("30:15", combineCookAndPrepTime("22:10", "08:05"))
    }

    @Test
    fun `Should use default value for empty strings`() {
        assertEquals("00:00", combineCookAndPrepTime("", ""))
    }

    @Test
    fun `Should handle no cook time`() {
        assertEquals("12:34", combineCookAndPrepTime("", "12:34"))
    }

    @Test
    fun `Should handle no prep time`() {
        assertEquals("23:59", combineCookAndPrepTime("23:59", ""))
    }

    @Test
    fun `Should increment hour after 59 minutes`() {
        assertEquals("01:20", combineCookAndPrepTime("00:35", "00:45"))
    }

    @Test
    fun `Should handle zero minutes`() {
        assertEquals("03:00", combineCookAndPrepTime("01:00", "02:00"))
    }
}