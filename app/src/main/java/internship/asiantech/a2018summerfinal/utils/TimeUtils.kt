package internship.asiantech.a2018summerfinal.utils

/**
 * util function to transfer time in int to string
 *
 * @param time : in millisecond
 * @return time in "m:hh" format
 */
fun timeToString(time: Int): String {
    var second = time / 1000
    val minutes = second / 60
    second %= 60
    val sec = if (second < 10) "0$second" else second.toString()
    return "$minutes:$sec"
}
