package internship.asiantech.a2018summerfinal.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.librarysong.ListMusicActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN
import com.google.firebase.database.*
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.model.SingletonUser
import internship.asiantech.a2018summerfinal.model.User

class LoginActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 100
        const val LOGIN_KEY = "is login"
        const val MAIL_KEY = "mail"
        const val PASSWORD_KEY = "password"
        const val USER = "user"
    }

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private var mail = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(LOGIN, MODE_PRIVATE)
        val check: Boolean = sharedPreferences.getBoolean(LOGIN_KEY, false)
        if (check) {
            mail = sharedPreferences.getString(MAIL_KEY, "")
            password = sharedPreferences.getString(PASSWORD_KEY, "")
            login(mail, password)
            return
        } else {
            setContentView(R.layout.activity_login)
        }
        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        btnLogin.setOnClickListener {
            mail = edtMail.text.toString()
            password = edtPassword.text.toString()
            login(mail, password)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val mail = data?.getStringExtra(SignUpActivity.MAIL_KEY)
            edtMail.text = Editable.Factory.getInstance().newEditable(mail)
        }
    }

    private fun login(mail: String, password: String) {
        if (mail == "" || password == "") {
            tvError.text = resources.getString(R.string.error_not_enough_information)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return
        }
        if (!SignUpActivity.UserValidate.mailValidate(mail)) {
            tvError.text = resources.getString(R.string.error_mail)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return
        }
        if (!SignUpActivity.UserValidate.passwordValidate(password)) {
            tvError.text = resources.getString(R.string.error_password)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return
        }
        auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        getCurrentUser(mail)
                        val intent = Intent(this, ProfileUserActivity::class.java)
                        startActivity(intent)
                    } else {
                        if (tvError != null) {
                            tvError.text = resources.getString(R.string.error_password)
                            tvError.setBackgroundResource(R.drawable.border_text_view_error)
                        }
                    }
                }
    }

    private fun getCurrentUser(mail: String) {
        val sharedPreferences = getSharedPreferences(LOGIN, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        database.child("Users").addChildEventListener(object : ValueEventListener, ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                if (user?.mail == mail) {
                    SingletonUser.instance.idUser = user.idUser
                    SingletonUser.instance.mail = user.mail
                    SingletonUser.instance.name = user.name
                    SingletonUser.instance.password = user.password
                    SingletonUser.instance.age = user.age
                    SingletonUser.instance.avatar = user.avatar
                    SingletonUser.instance.latitude = user.latitude
                    SingletonUser.instance.longitude = user.longitude
                    val gson = Gson()
                    val json = gson.toJson(user)
                    editor.putString(USER, json)
                    editor.commit()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

            override fun onDataChange(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
