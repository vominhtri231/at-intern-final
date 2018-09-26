package internship.asiantech.a2018summerfinal.utils

fun trimStringToLength(input: String, bound: Int): String {
    return if (input.length < bound) input else input.substring(0, bound)
}
