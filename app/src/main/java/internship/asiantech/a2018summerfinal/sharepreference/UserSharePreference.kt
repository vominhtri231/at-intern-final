package internship.asiantech.a2018summerfinal.sharepreference

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import internship.asiantech.a2018summerfinal.model.User

class UserSharePreference(val context: Context) {
    companion object {
        private val TAG = UserSharePreference::class.qualifiedName
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
        val json: String = Gson().toJson(user)
        editor.putString(USER, json)
        Log.e(TAG, "save json : $json")
        editor.apply()
    }

    fun changeAvatar(avatar: String) {
        val user: User? = getCurrentUser()
        user?.let {
            user.avatarPath = avatar
            val editor = sharedPreferences.edit()
            val json: String = Gson().toJson(user)
            editor.putString(USER, json)
            editor.apply()
        }
    }

    fun getCurrentUser(): User? {
        val json = sharedPreferences.getString(USER, "")
        Log.e(TAG, "get json : $json")
        try {
            return Gson().fromJson<User>(json, User::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, e.localizedMessage)
            return null
        }
    }

    fun removeUserCurrent() {
        sharedPreferences.edit().clear().apply()
    }
}
