package recipeapplication.utility;

public class IngredientConverter
{
    private static final double OUNCE_TO_GRAM = 28.35;
    private static final double KILOGRAM_TO_POUND = 2.205;
    private static final String OUNCE = "oz";
    private static final String GRAM = "g";
    private static final String KILOGRAM = "kg";
    private static final String POUND = "lbs";

    public static String toImperial(String quantity)
    {
        if (quantity == null)
        {
            return null;
        }

        String[] words = quantity.split(" ");

        int index = 0;
        for (String word : words)
        {
            try
            {
                if (word.toLowerCase().endsWith(KILOGRAM))
                {
                    double value = extractValue(word, KILOGRAM);
                    value = value * KILOGRAM_TO_POUND;
                    words[index] = roundOneDecimalPlace(value) + POUND;

                } else if (word.toLowerCase().endsWith(GRAM))
                {
                    double value = extractValue(word, GRAM);
                    value = value / OUNCE_TO_GRAM;
                    words[index] = roundOneDecimalPlace(value) + OUNCE;
                }
            }
            catch (NumberFormatException e)
            {
                // If a number cannot be extracted we move on to the next word
            }

            index++;
        }

        return String.join(" ", words);
    }

    public static String toMetric(String quantity)
    {
        if (quantity == null)
        {
            return null;
        }

        String[] words = quantity.split(" ");

        int index = 0;
        for (String word : words)
        {
            try
            {
                if (word.toLowerCase().endsWith(POUND))
                {
                    double value = extractValue(word, POUND);
                    value = value / KILOGRAM_TO_POUND;
                    words[index] = roundOneDecimalPlace(value) + KILOGRAM;

                } else if (word.toLowerCase().endsWith(OUNCE))
                {
                    double value = extractValue(word, OUNCE);
                    value = value * OUNCE_TO_GRAM;
                    value = Math.round(value);
                    words[index] = ((int) value) + GRAM;
                }
            }
            catch (NumberFormatException e)
            {
                // If a number cannot be extracted we move on to the next word
            }

            index++;
        }

        return String.join(" ", words);
    }

    private static double extractValue(String word, String measurement)
    {
        return Double.parseDouble(word.substring(0, word.length() - measurement.length()));
    }

    private static double roundOneDecimalPlace(double value)
    {
        return Math.round(value * 10.0) / 10.0;
    }
}
