package thecookingpot.utility

private const val DEFAULT_VALUE = "00:00"

fun combineCookAndPrepTime(cookTime: String, prepTime: String): String {
    if (cookTime.isEmpty() && prepTime.isEmpty()) {
        return DEFAULT_VALUE
    }
    if (cookTime.isEmpty()) {
        return prepTime
    } else if (prepTime.isEmpty()) {
        return cookTime
    }

    val totalTime = addCookAndPrepTime(
            cookTime.replace(":", ""),
            prepTime.replace(":", "")
    )

    return addColonToTime(totalTime)
}

private fun zeroPadTime4DP(string: String): String {
    return "%04d".format(string.toInt())
}

private fun zeroPadTime2DP(time: Int): String {
    return "%02d".format(time)
}

private fun addCookAndPrepTime(cookTime: String, prepTime: String): String {
    val totalTime = (cookTime.toInt() + prepTime.toInt()).toString()
    var totalTimeMinutes = getMinuteFromTime(totalTime)
    var totalTimeHours = getHourFromTime(totalTime)
    if (totalTimeMinutes > 59) {
        totalTimeMinutes -= 60
        totalTimeHours++
    }
    return zeroPadTime2DP(totalTimeHours) + zeroPadTime2DP(totalTimeMinutes)
}

private fun addColonToTime(time: String): String {
    return time.substring(0, 2) + ":" + time.substring(2)
}

private fun getHourFromTime(time: String): Int {
    val hoursAsString = zeroPadTime4DP(time).substring(0, 2)
    return hoursAsString.toInt()
}

private fun getMinuteFromTime(time: String): Int {
    val zeroPaddedTime = zeroPadTime4DP(time)
    return zeroPaddedTime.substring(zeroPaddedTime.length - 2).toInt()
}
