package internship.asiantech.a2018summerfinal.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import kotlinx.android.synthetic.main.activity_profile_user.*
import java.io.IOException

@Suppress("DEPRECATION")
class ProfileUserActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private lateinit var user: User
    private lateinit var tvError: TextView
    private lateinit var edtNewPassword: EditText
    private lateinit var edtRepeatPassword: EditText
    private var isSave = false
    private var age: Int = 0

    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_GALLERY = 2
        private const val URL_IMAGE = "gs://a2018summerfinal-76c35.appspot.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        initMap()
        initUser()
        btnEdit.setOnClickListener {
            edtNewPassword = EditText(this)
            edtRepeatPassword = EditText(this)
            if (!isSave) {
                createEditText(edtNewPassword, resources.getString(R.string.prompt_new_password), 5)
                createEditText(edtRepeatPassword, resources.getString(R.string.prompt_repeat_password), 6)
                edtName.isEnabled = true
                edtAge.isEnabled = true
                edtPassword.isEnabled = true
                edtPassword.setText("")
                edtPassword.hint = resources.getString(R.string.prompt_old_password)
                btnEdit.text = resources.getString(R.string.action_change)
            } else {
                btnEdit.text = resources.getString(R.string.action_edit)
                edtName.isEnabled = false
                edtAge.isEnabled = false
                edtPassword.isEnabled = false
                llProfileUser.removeViewAt(5)
                llProfileUser.removeViewAt(5)
                updateUSer()
            }
            isSave = !isSave
        }
    }

    private fun updateUSer() {
        val name = edtName.text.toString()
        val oldPassword = edtPassword.text.toString()
        val newPassword = edtNewPassword.text.toString()
        val repeatPassword = edtRepeatPassword.text.toString()
        val ageString = edtAge.text.toString()
        if (checkUser(name, oldPassword, newPassword, repeatPassword, ageString)) {
            Toast.makeText(this, "Update success!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUser(name: String, oldPassword: String, newPassword: String, repeatPassword: String, ageString: String): Boolean {
        if (ageString == "" || oldPassword == "" || name == "" || newPassword == "" || repeatPassword == "") {
            createTextViewError(resources.getString(R.string.error_not_enough_information))
            return false
        }
        if (newPassword != repeatPassword) {
            createTextViewError(resources.getString(R.string.error_password_not_match_repeat_password))
            return false
        }
        try {
            age = ageString.toInt()
        } catch (e: Exception) {
            createTextViewError(resources.getString(R.string.error_age))
            return false
        }
        if (!SignUpActivity.UserValidate.ageValidate(age)) {
            createTextViewError(resources.getString(R.string.error_age))
            return false
        }

        if (!SignUpActivity.UserValidate.nameValidate(name)) {
            createTextViewError(resources.getString(R.string.error_name))
            return false
        }

        if (!SignUpActivity.UserValidate.passwordValidate(newPassword)) {
            createTextViewError(resources.getString(R.string.error_password))
            return false
        }

        if (!SignUpActivity.UserValidate.passwordValidate(oldPassword) || edtPassword.text.toString() != user.password) {
            createTextViewError(resources.getString(R.string.error_password))
            return false
        }
        return true
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun createTextViewError(message: String) {
        tvError = TextView(this)
        tvError.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)
        tvError.text = message
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
        tvError.setPadding(16, 6, 16, 6)
        tvError.setTextColor(resources.getColor(R.color.colorMonza))
        llProfileUser.addView(tvError, 8)
    }

    private fun createEditText(edt: EditText, hint: String, index: Int) {
        edt.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        edt.hint = hint
        edt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        llProfileUser.addView(edt, index)
    }

    private fun initUser() {
        val sharedPreferences = getSharedPreferences(FirebaseAnalytics.Event.LOGIN, MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(LoginActivity.USER, "")
        user = gson.fromJson<User>(json, User::class.java)
        edtMail.text = Editable.Factory.getInstance().newEditable(user.mail)
        edtName.text = Editable.Factory.getInstance().newEditable(user.name)
        edtAge.text = Editable.Factory.getInstance().newEditable(user.age.toString())
        edtPassword.text = Editable.Factory.getInstance().newEditable(user.password)
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val location = LatLng(user.latitude, user.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
        map.uiSettings.isZoomControlsEnabled = true
        map.addMarker(MarkerOptions().position(location)
                .title(resources.getString(R.string.your_location)))
    }

    override fun onMapClick(p0: LatLng?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri?
            if (requestCode == PICK_FROM_CAMERA) {
                if (data.extras != null) {
                    val bp = data.extras?.get("data") as Bitmap
                    imgAvatar.setImageBitmap(bp)
                }
            } else {
                uri = data.data
                try {
                    val bp = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imgAvatar.setImageBitmap(bp)
                } catch (e: IOException) {
                    Toast.makeText(this, "Error load gallery!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
