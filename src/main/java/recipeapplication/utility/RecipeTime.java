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
        totalTime = addColonToTime(totalTime);

        return totalTime;
    }

    private static String zeroPadTime4DP(String string)
    {
        return String.format("%04d", Integer.parseInt(string));
    }

    private static String zeroPadTime2DP(int time)
    {
        return String.format("%02d", time);
    }

    private static String addCookAndPrepTime(String cookTime, String prepTime)
    {
        String totalTime = Integer.toString(Integer.parseInt(cookTime) + Integer.parseInt(prepTime));

        int totalTimeMinutes = getMinuteFromTime(totalTime);
        int totalTimeHours = getHourFromTime(totalTime);

        if (totalTimeMinutes > 59)
        {
            totalTimeMinutes = totalTimeMinutes - 60;
            totalTimeHours++;
        }

        return zeroPadTime2DP(totalTimeHours) + zeroPadTime2DP(totalTimeMinutes);
    }

    private static String addColonToTime(String time)
    {
        return time.substring(0, 2) + ":" + time.substring(2);
    }

    private static int getHourFromTime(String time)
    {
        String hoursAsString = zeroPadTime4DP(time).substring(0, 2);

        if (!hoursAsString.equals(""))
        {
            return Integer.parseInt(hoursAsString);
        }

        return 0;
    }

    private static int getMinuteFromTime(String time)
    {
        String zeroPaddedTime = zeroPadTime4DP(time);

        return Integer.parseInt(zeroPaddedTime.substring(zeroPaddedTime.length() -2));
    }
}
