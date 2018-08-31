package internship.asiantech.a2018summerfinal.validate

import java.util.regex.Matcher
import java.util.regex.Pattern

class UserValidate {
    companion object {
        fun phoneValidate(phone: String): Boolean {
            val validatePhone =
                    Pattern.compile("[0-9]{8,16}$", Pattern.CASE_INSENSITIVE)
            val matcher = validatePhone.matcher(phone)
            return matcher.find()
        }

        fun nameValidate(name: String): Boolean {
            val validatePhone =
                    Pattern.compile("[a-zA-Z]{2,40}$", Pattern.CASE_INSENSITIVE)
            val matcher = validatePhone.matcher(name)
            return matcher.find()
        }

        fun passwordValidate(password: String): Boolean {
            val validatePhone =
                    Pattern.compile("[a-zA-Z0-9]{6,}", Pattern.CASE_INSENSITIVE)
            val matcher = validatePhone.matcher(password)
            return matcher.find()
        }

        fun ageValidate(age: Int): Boolean {
            return age in 10..100
        }
    }
}
