package internship.asiantech.a2018summerfinal

import internship.asiantech.a2018summerfinal.utils.isWordInString
import org.junit.Test
import org.mockito.internal.util.StringUtil

class CheckWorkInString {
    @Test
    fun hasWorkInString(){
        val searchStr="a"
        val str="asjd qwe fqw"
        assert(isWordInString(str,searchStr))
    }

    @Test
    fun hasNoWorkInString(){
        val searchStr="a"
        val str="sjd t ttqwe fqw"
        assert(!isWordInString(str,searchStr))
    }
}