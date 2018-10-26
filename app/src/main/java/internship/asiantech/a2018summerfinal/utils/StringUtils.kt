package internship.asiantech.a2018summerfinal.utils

fun trimStringToLength(input: String, bound: Int): String {
    return if (input.length < bound) input else input.substring(0, bound)
}

fun isWordInString(str: String, keySearch: String): Boolean {
    val words = str.toLowerCase().split(' ')
    for (word in words) {
        if (word.contains(keySearch.toLowerCase())) {
            return true
        }
    }
    return false
}
