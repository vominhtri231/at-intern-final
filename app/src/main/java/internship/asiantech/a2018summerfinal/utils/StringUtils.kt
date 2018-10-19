package internship.asiantech.a2018summerfinal.utils

fun trimStringToLength(input: String, bound: Int): String {
    return if (input.length < bound) input else input.substring(0, bound)
}

fun isWordInString(strSearch: String, str: String): Boolean {
    val words = strSearch.split(" ".toRegex())
    for (word in words) {
        return str.toLowerCase().contains(word.toLowerCase())
    }
    return false
}
