package internship.asiantech.a2018summerfinal

import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.utils.checkUserSignUp
import org.junit.Test

class CheckSignUpUnitTest {
    @Test
    fun checkEmptyInvalid() {
        val user = User(mail = "ff@gmail.com", password = "12456", name = "")
        val res = checkUserSignUp(user, "12456", "12")
        assert(!res.isSuccess())
        assert(res.getMessage().equals(R.string.error_not_enough_information))
    }

    @Test
    fun checkNameInvalid() {
        val user = User(mail = "ff@gmail.com", password = "123456", name = "le ff%%")
        val res = checkUserSignUp(user, "123456", "12")
        assert(!res.isSuccess())
        assert(res.getMessage().equals(R.string.error_name))
    }

    @Test
    fun checkPasswordInvalid() {
        val user = User(mail = "ff@gmail.com", password = "12456", name = "leff")
        val res = checkUserSignUp(user, "12456", "12")
        assert(!res.isSuccess())
        assert(res.getMessage().equals(R.string.error_password))
    }

    @Test
    fun checkAgeInvalid() {
        val user = User(mail = "ff@gmail.com", password = "12456", name = "leff")
        val res = checkUserSignUp(user, "12456", "12ff")
        assert(!res.isSuccess())
        assert(res.getMessage().equals(R.string.error_age))
    }

    @Test
    fun checkPasswordMatchInvalid() {
        val user = User(mail = "ff@gmail.com", password = "12456", name = "leff")
        val res = checkUserSignUp(user, "124567", "12ff")
        assert(!res.isSuccess())
        assert(res.getMessage().equals(R.string.error_password_not_match_repeat_password))
    }

    @Test
    fun checkValid() {
        val user = User(mail = "ff@gmail.com", password = "123456", name = "leff")
        val res = checkUserSignUp(user, "123456", "12")
        assert(res.isSuccess())
        assert(res.getMessage().equals(R.string.success))
    }
}
