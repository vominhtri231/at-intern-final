package internship.asiantech.a2018summerfinal.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.firebase.AuthUpdater
import internship.asiantech.a2018summerfinal.firebase.FirebaseAuthUtils
import internship.asiantech.a2018summerfinal.firebase.FirebaseDatabaseUtils
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.utils.checkUserSignUp
import kotlinx.android.synthetic.main.activity_sign_up.*

@SuppressLint("Registered")
@Suppress("DEPRECATED_IDENTITY_EQUALS", "UNUSED_EXPRESSION")
class SignUpActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private var location: LatLng = LatLng(16.0544, 108.2022)

    companion object {
        const val MAIL_KEY = "SIGN_UP_MAIL_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initListeners()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val danang = LatLng(16.0544, 108.2022)
        map.moveCamera(CameraUpdateFactory.newLatLng(danang))
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng?) {
        p0?.let {
            location = it
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun initListeners() {
        btnSignUp.setOnClickListener {
            val mail = edtMail.text.toString()
            val name = edtName.text.toString()
            val password = edtPassword.text.toString()
            val repeatPassword = edtRepeatPassword.text.toString()
            val ageString = edtAge.text.toString()

            val newUser = User(mail = mail, password = password, name = name,
                    longitude = location.longitude, latitude = location.latitude)
            signUp(newUser, repeatPassword, ageString)
        }
    }

    private fun signUp(user: User, repeatPassword: String, ageString: String) {
        val result = checkUserSignUp(user, repeatPassword, ageString)
        if (!result.isSuccess()) {
            displayErrorMessage(result.getMessage())
        } else {
            user.age = ageString.toInt()
            createUser(user)
        }
    }

    private fun createUser(user: User) {
        FirebaseAuthUtils.createUser(user.mail, user.password, this, object : AuthUpdater {
            override fun onSuccess() {
                addUserInfoToDatabase(user)
                returnLoginActivityWithMail(user.mail)
            }

            override fun onFail() {
                displayErrorMessage(R.string.error_sign_up)
            }
        })
    }


    private fun addUserInfoToDatabase(user: User) {
        FirebaseDatabaseUtils.addUser(user)
    }

    private fun returnLoginActivityWithMail(mail: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(MAIL_KEY, mail)
        setResult(LoginActivity.REQUEST_CODE, intent)
        finish()
    }

    private fun displayErrorMessage(messageReference: Int) {
        tvError.text = resources.getString(messageReference)
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
    }
}
