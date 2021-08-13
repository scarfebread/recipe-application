package thecookingpot.recipe.utility

import org.junit.Test
import kotlin.test.assertEquals

class IngredientConverterTest {
    @Test
    fun `Should convert gram to ounce`() {
        val input = "500g of chicken thigh"
        val output = "17.6oz of chicken thigh"
        assertEquals(output, toImperial(input))
    }

    @Test
    fun `Should convert ounce to gram`() {
        val input = "Another 10oz of chicken thigh"
        val output = "Another 284g of chicken thigh"
        assertEquals(output, toMetric(input))
    }

    @Test
    fun `Should convert kilogram to pound`() {
        val input = "Chicken thigh 2.5kg"
        val output = "Chicken thigh 5.5lbs"
        assertEquals(output, toImperial(input))
    }

    @Test
    fun `Should convert pound to kilogram`() {
        val input = "Chicken thigh 5lbs"
        val output = "Chicken thigh 2.3kg"
        assertEquals(output, toMetric(input))
    }

    @Test
    fun `Should not edit value if not in correct format in toMetric`() {
        val input = "Chicken thigh fivelbs"
        val output = "Chicken thigh fivelbs"
        assertEquals(output, toMetric(input))
    }

    @Test
    fun `Should not edit value if not in correct format in toImperial`() {
        val input = "Chicken thigh bkg"
        val output = "Chicken thigh bkg"
        assertEquals(output, toImperial(input))
    }
}
