package internship.asiantech.a2018summerfinal.utils

import android.nfc.FormatException
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import java.util.regex.Pattern

fun checkUserLogin(email: String, password: String): ValidateResult {
    if (email == "" || password == "") {
        return ValidateResult(R.string.error_not_enough_information)
    }
    if (!isValidMail(email)) {
        return ValidateResult(R.string.error_mail)
    }
    if (!isValidPassword(password)) {
        return ValidateResult(R.string.error_password)
    }
    return ValidateResult(null)
}

fun checkUserUpdate(name: String, oldPassword: String, newPassword: String,
                    repeatPassword: String, ageString: String): ValidateResult {
    if (ageString == "" || oldPassword == "" || name == "" || newPassword == "" || repeatPassword == "") {
        return ValidateResult(R.string.error_not_enough_information)
    }
    if (newPassword != repeatPassword) {
        return ValidateResult(R.string.error_password_not_match_repeat_password)
    }
    if (!isValidAge(ageString)) {
        return ValidateResult(R.string.error_age)
    }
    if (!isValidName(name)) {
        return ValidateResult(R.string.error_name)
    }
    if (!isValidPassword(newPassword)) {
        return ValidateResult(R.string.error_password)
    }
    if (newPassword != oldPassword) {
        return ValidateResult(R.string.error_password)
    }
    return ValidateResult(null)
}

fun checkUserSignUp(user: User, repeatPassword: String, ageString: String): ValidateResult {
    if (ageString == "" || user.mail == "" || user.name == "" || user.password == "" || repeatPassword == "") {
        return ValidateResult(R.string.error_not_enough_information)
    }
    if (user.password != repeatPassword) {
        return ValidateResult(R.string.error_password_not_match_repeat_password)
    }
    if (!isValidAge(ageString)) {
        return ValidateResult(R.string.error_age)
    }
    if (!isValidName(user.name)) {
        return ValidateResult(R.string.error_name)
    }
    if (!isValidPassword(user.password)) {
        return ValidateResult(R.string.error_password)
    }
    if (!isValidMail(user.mail)) {
        return ValidateResult(R.string.error_mail)
    }
    return ValidateResult(null)
}

private fun isValidMail(mail: String): Boolean {
    val validateMail =
            Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9._%+-]*+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    val matcher = validateMail.matcher(mail)
    return matcher.find()
}

private fun isValidName(name: String): Boolean {
    val validateName =
            Pattern.compile("[a-zA-Z]{2,40}$", Pattern.CASE_INSENSITIVE)
    val matcher = validateName.matcher(name)
    return matcher.find()
}

private fun isValidPassword(password: String): Boolean {
    val validatePassword =
            Pattern.compile("[a-zA-Z0-9]{6,}", Pattern.CASE_INSENSITIVE)
    val matcher = validatePassword.matcher(password)
    return matcher.find()
}

private fun isValidAge(ageString: String): Boolean {
    try {
        val age = ageString.toInt()
        if (age in 10..100) {
            return true
        }
    } catch (e: NumberFormatException) {
        return false
    }
    return false
}

class ValidateResult(private val message: Int?) {
    fun isSuccess(): Boolean {
        return message == null
    }

    fun getMessage(): Int {
        return if (message != null) message else R.string.success
    }
}
