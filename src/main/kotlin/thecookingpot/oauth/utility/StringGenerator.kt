package thecookingpot.oauth.utility

private const val CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"

fun generateString(length: Int): String {
    return (1..length).map { CHAR_SET.random() }.joinToString("")
}