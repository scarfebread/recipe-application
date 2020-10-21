package recipeapplication.utility

import kotlin.math.roundToInt

private const val OUNCE_TO_GRAM = 28.35;
private const val KILOGRAM_TO_POUND = 2.205;
private const val OUNCE = "oz";
private const val GRAM = "g";
private const val KILOGRAM = "kg";
private const val POUND = "lbs";

fun toImperial(quantity: String) : String {
    val words = quantity.split(" ").toMutableList()
    words.forEachIndexed { index, word ->
        try {
            if (word.toLowerCase().endsWith(KILOGRAM)) {
                val value = extractValue(word, KILOGRAM) * KILOGRAM_TO_POUND
                words[index] = roundOneDecimalPlace(value).toString() + POUND
            } else if (word.toLowerCase().endsWith(GRAM)) {
                val value = extractValue(word, GRAM) / OUNCE_TO_GRAM
                words[index] = roundOneDecimalPlace(value).toString() + OUNCE
            }
        } catch (e: NumberFormatException) {
            // If a number cannot be extracted we move on to the next word
        }
    }

    return words.joinToString(separator = " ")
}

fun toMetric(quantity: String): String {
    val words = quantity.split(" ").toMutableList()
    words.forEachIndexed { index, word ->
        try {
            if (word.toLowerCase().endsWith(POUND)) {
                val value = extractValue(word, POUND) / KILOGRAM_TO_POUND
                words[index] = roundOneDecimalPlace(value).toString() + KILOGRAM
            } else if (word.toLowerCase().endsWith(OUNCE)) {
                val value = (extractValue(word, OUNCE) * OUNCE_TO_GRAM).roundToInt().toDouble()
                words[index] = value.toInt().toString() + GRAM
            }
        } catch (e: NumberFormatException) {
            // If a number cannot be extracted we move on to the next word
        }
    }

    return words.joinToString(separator = " ")
}

private fun extractValue(word: String, measurement: String): Double {
    return word.substring(0, word.length - measurement.length).toDouble()
}

private fun roundOneDecimalPlace(value: Double): Double {
    return (value * 10.0).roundToInt() / 10.0
}
