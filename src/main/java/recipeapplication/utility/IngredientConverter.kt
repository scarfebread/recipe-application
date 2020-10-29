package recipeapplication.utility

import kotlin.math.roundToInt

private const val OUNCE_TO_GRAM = 28.35
private const val KILOGRAM_TO_POUND = 2.205
private const val OUNCE = "oz"
private const val GRAM = "g"
private const val KILOGRAM = "kg"
private const val POUND = "lbs"

fun toImperial(ingredient: String) : String {
    return convertMeasurements(ingredient) { word ->
        when (true) {
            word.toLowerCase().endsWith(KILOGRAM) -> {
                val value = (word extractValue  KILOGRAM) * KILOGRAM_TO_POUND
                roundOneDecimalPlace(value).toString() + POUND
            }
            word.toLowerCase().endsWith(GRAM) -> {
                val value = (word extractValue  GRAM) / OUNCE_TO_GRAM
                roundOneDecimalPlace(value).toString() + OUNCE
            }
            else -> word
        }
    }
}

fun toMetric(ingredient: String): String {
    return convertMeasurements(ingredient) { word ->
        when (true) {
            word.toLowerCase().endsWith(POUND) -> {
                val value = (word extractValue  POUND) / KILOGRAM_TO_POUND
                roundOneDecimalPlace(value).toString() + KILOGRAM
            }
            word.toLowerCase().endsWith(OUNCE) -> {
                val value = ((word extractValue OUNCE) * OUNCE_TO_GRAM).roundToInt()
                value.toString() + GRAM
            }
            else -> word
        }
    }
}

private infix fun String.extractValue(measurement: String): Double {
    return this.substring(0, this.length - measurement.length).toDouble()
}

private fun roundOneDecimalPlace(value: Double): Double {
    return (value * 10.0).roundToInt() / 10.0
}

private fun convertMeasurements(ingredient: String, convert: (String) -> String) : String {
    val words = ingredient.split(" ")
    return words.joinToString(separator = " ") { word ->
        try {
            convert(word)
        } catch (e: NumberFormatException) {
            word
        }
    }
}
