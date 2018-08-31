package internship.asiantech.a2018summerfinal.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import internship.asiantech.a2018summerfinal.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
