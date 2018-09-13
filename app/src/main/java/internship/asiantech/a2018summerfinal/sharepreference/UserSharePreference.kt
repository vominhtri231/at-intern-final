package internship.asiantech.a2018summerfinal.sharepreference

import android.content.Context
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.model.User

class UserSharePreference(val context: Context) {
    companion object {
        const val LOGIN_KEY = "is login"
        const val USER = "user"
        const val LOGIN = "login"
    }

    private val sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_KEY, false)
    }

    fun saveUserLogin(user: User) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGIN_KEY, true)
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(USER, json)
        editor.apply()
    }

    fun getCurrentUser(): User {
        val gson = Gson()
        val json = sharedPreferences.getString(USER, "")
        return gson.fromJson<User>(json, User::class.java)
    }

    fun removeUserCurrent() {
        sharedPreferences.edit().clear().apply()
    }
}
