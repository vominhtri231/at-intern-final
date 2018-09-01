package internship.asiantech.a2018summerfinal.validate

import java.util.regex.Pattern

class UserValidate {
    companion object {
        fun mailValidate(mail: String): Boolean {
            val validateMail =
                    Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9._%+-]*+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
            val matcher = validateMail.matcher(mail)
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
