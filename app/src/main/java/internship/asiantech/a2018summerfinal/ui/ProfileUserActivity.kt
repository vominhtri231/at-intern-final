package internship.asiantech.a2018summerfinal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import kotlinx.android.synthetic.main.activity_profile_user.*

class ProfileUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        val sharedPreferences = getSharedPreferences(FirebaseAnalytics.Event.LOGIN, MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(LoginActivity.USER, "")
        val user = gson.fromJson<User>(json, User::class.java)
        edtMail.text = Editable.Factory.getInstance().newEditable(user.mail)
        edtMail.text = Editable.Factory.getInstance().newEditable(user.mail)
        edtMail.text = Editable.Factory.getInstance().newEditable(user.mail)
        edtMail.text = Editable.Factory.getInstance().newEditable(user.mail)

    }
}
