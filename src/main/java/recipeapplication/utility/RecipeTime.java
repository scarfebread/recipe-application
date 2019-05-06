package recipeapplication.utility;

public class RecipeTime
{
    private static final String DEFAULT_VALUE = "00:00";

    public static String combineCookAndPrepTime(String cookTime, String prepTime)
    {
        // TODO I might want to handle invalid formats

        if (cookTime.isEmpty() && prepTime.isEmpty())
        {
            return DEFAULT_VALUE;
        }

        if (cookTime.isEmpty())
        {
            cookTime = DEFAULT_VALUE;
        }
        else if (prepTime.isEmpty())
        {
            prepTime = DEFAULT_VALUE;
        }

        cookTime = cookTime.replace(":", "");
        prepTime = prepTime.replace(":", "");

        String totalTime = addCookAndPrepTime(cookTime, prepTime);
        totalTime = zeroPadCookTime(totalTime);
        totalTime = addColonToTime(totalTime);

        return totalTime;
    }

    private static String zeroPadCookTime(String string)
    {
        return String.format("%04d", Integer.parseInt(string));
    }

    private static String addCookAndPrepTime(String cookTime, String prepTime)
    {
        return Integer.toString(Integer.parseInt(cookTime) + Integer.parseInt(prepTime));
    }

    private static String addColonToTime(String time)
    {
        return time.substring(0, 2) + ":" + time.substring(2);
    }
}
