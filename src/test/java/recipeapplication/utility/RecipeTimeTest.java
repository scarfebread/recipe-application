package recipeapplication.utility;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RecipeTimeTest
{
    @Test
    public void shouldCombineValidCookAndPrepTimes()
    {
        assertEquals("21:52", RecipeTime.combineCookAndPrepTime("10:21", "11:31"));
    }

    @Test
    public void shouldCombineLargeCookAndPrepTimes()
    {
        assertEquals("30:15", RecipeTime.combineCookAndPrepTime("22:10", "08:05"));
    }

    @Test
    public void shouldUseDefaultValueForEmptyStrings()
    {
        assertEquals("00:00", RecipeTime.combineCookAndPrepTime("", ""));
    }

    @Test
    public void shouldHandleNoCookTime()
    {
        assertEquals("12:34", RecipeTime.combineCookAndPrepTime("", "12:34"));
    }

    @Test
    public void shouldHandleNoPrepTime()
    {
        assertEquals("23:59", RecipeTime.combineCookAndPrepTime("23:59", ""));
    }
}
