package recipeapplication.utility;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class IngredientConverterTest
{
    @Test
    public void shouldConvertGramToOunce()
    {
        String input = "500g of chicken thigh";
        String output = "17.6oz of chicken thigh";

        assertEquals(output, IngredientConverter.toImperial(input));
    }

    @Test
    public void shouldConvertOunceToGram()
    {
        String input = "Another 10oz of chicken thigh";
        String output = "Another 284g of chicken thigh";

        assertEquals(output, IngredientConverter.toMetric(input));
    }

    @Test
    public void shouldConvertKilogramToPound()
    {
        String input = "Chicken thigh 2.5kg";
        String output = "Chicken thigh 5.5lbs";

        assertEquals(output, IngredientConverter.toImperial(input));
    }

    @Test
    public void shouldConvertPoundToKilogram()
    {
        String input = "Chicken thigh 5lbs";
        String output = "Chicken thigh 2.3kg";

        assertEquals(output, IngredientConverter.toMetric(input));
    }

    @Test
    public void shouldNotEditValueIfNotInCorrectFormatInToMetric()
    {
        String input = "Chicken thigh fivelbs";
        String output = "Chicken thigh fivelbs";

        assertEquals(output, IngredientConverter.toMetric(input));
    }

    @Test
    public void shouldNotEditValueIfNotInCorrectFormatInToImperial()
    {
        String input = "Chicken thigh bkg";
        String output = "Chicken thigh bkg";

        assertEquals(output, IngredientConverter.toImperial(input));
    }

    @Test
    public void shouldReturnNullIfInputIsNullForToImperial()
    {
        assertNull(IngredientConverter.toImperial(null));
    }

    @Test
    public void shouldReturnNullIfInputIsNullForToMetric()
    {
        assertNull(IngredientConverter.toMetric(null));
    }
}
