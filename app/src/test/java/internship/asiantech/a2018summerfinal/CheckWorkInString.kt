package internship.asiantech.a2018summerfinal

import internship.asiantech.a2018summerfinal.utils.isWordInString
import org.junit.Test

class CheckWorkInString {
    @Test
    fun hasWorkInStringMiddle(){
        val searchStr="a"
        val str="sjd qawe fqw"
        assert(isWordInString(str,searchStr))
    }

    @Test
    fun hasWorkInStringStart(){
        val searchStr="a"
        val str="sjad qwe fqw"
        assert(isWordInString(str,searchStr))
    }

    @Test
    fun hasNoWorkInString(){
        val searchStr="a"
        val str="sjd t ttqwe fqw"
        assert(!isWordInString(str,searchStr))
    }
}