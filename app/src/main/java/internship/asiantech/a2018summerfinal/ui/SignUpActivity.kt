package internship.asiantech.a2018summerfinal.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.validate.UserValidate
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SignUpActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var repeatPassword: String
    private lateinit var avatar: Bitmap
    private var age: Int = 0
    private var location: LatLng = LatLng(16.0544, 108.2022)
    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val danang = LatLng(16.0544, 108.2022)
        map.moveCamera(CameraUpdateFactory.newLatLng(danang))
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
        btnSignUp.setOnClickListener {
            phone = edtPhone.text.toString()
            name = edtName.text.toString()
            password = edtPassword.text.toString()
            repeatPassword = edtRepeatPassword.text.toString()
            val ageString = edtRepeatPassword.text.toString()
            checkUser(phone, name, password, repeatPassword, ageString)
        }

        imgAvatar.setOnClickListener {
            dialog()
        }
    }

    override fun onMapClick(p0: LatLng?) {
        p0?.let {
            location = it
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun checkUser(phone: String, name: String, password: String, repeatPassword: String, ageString: String): Boolean {
        if (ageString == "" || phone == "" || name == "" || password == "" || repeatPassword == "") {
            tvError.text = resources.getString(R.string.not_enough_information)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return false
        }
        if (password != repeatPassword) {
            tvError.text = resources.getString(R.string.not_enough_information)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return false
        }
        try {
            age = ageString.toInt()
        } catch (e: Exception) {
            tvError.text = resources.getString(R.string.error_age)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return false
        }
        if (!UserValidate.ageValidate(age)) {
            tvError.text = resources.getString(R.string.error_age)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
        }
        if (!UserValidate.phoneValidate(phone)) {
            tvError.text = resources.getString(R.string.error_phone)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
        }
        if (!UserValidate.nameValidate(name)) {
            tvError.text = resources.getString(R.string.error_name)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
        }
        if (!UserValidate.passwordValidate(password)) {
            tvError.text = resources.getString(R.string.error_password)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
        }
        return true
    }

    private fun dialog() {
        val items = arrayOf("Camera", "Gallery")
        val adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, items)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setAdapter(adapter) { _, which ->
            if (which === 0) {
                if (ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PICK_FROM_CAMERA)
                } else {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(packageManager) != null) {
                        startActivityForResult(cameraIntent, PICK_FROM_CAMERA)
                    }
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PICK_FROM_GALLERY)
                } else {
                    val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY)
                }
            }
        }
        builder.create()
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri?
            if (requestCode == PICK_FROM_CAMERA) {
                if (data.extras != null) {
                    val bp = data.extras!!.get("data") as Bitmap
                    imgAvatar.setImageBitmap(bp)
                    avatar=bp
                }
            } else {
                uri = data.data
                try {
                    val bp = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imgAvatar.setImageBitmap(bp)
                    avatar=bp
                } catch (e: IOException) {
                    Toast.makeText(this, "Error load gallery!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
