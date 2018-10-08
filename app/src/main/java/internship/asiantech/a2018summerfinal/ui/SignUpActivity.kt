package internship.asiantech.a2018summerfinal.ui

import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.firebase.*
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.utils.checkUserSignUp
import internship.asiantech.a2018summerfinal.utils.getBitmapFromImageView
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException

@SuppressLint("Registered")
@Suppress("DEPRECATED_IDENTITY_EQUALS", "UNUSED_EXPRESSION")
class SignUpActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private var location: LatLng = LatLng(16.0544, 108.2022)

    companion object {
        private val TAG = SignUpActivity::class.qualifiedName
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_GALLERY = 2
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

        imgAvatar.setOnClickListener {
            createDialog()
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
                insertUserInfoToDatabase(user)
                returnLoginActivityWithMail(user.mail)
            }

            override fun onFail() {
                displayErrorMessage(R.string.error_sign_up)
            }

        })
    }

    private fun insertUserInfoToDatabase(user: User) {
        FirebaseStorageUtils.uploadImage(getBitmapFromImageView(imgAvatar), object : StorageUpdater {
            override fun onSuccess(uri: Uri) {
                Log.e(TAG, uri.toString())
                user.avatar = uri.toString()
                FirebaseDatabaseUtils.addUser(user)
            }

            override fun onFail() {
                Toast.makeText(this@SignUpActivity, resources.getString(R.string.error_upload_image), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun returnLoginActivityWithMail(mail: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(MAIL_KEY, mail)
        setResult(LoginActivity.REQUEST_CODE, intent)
        finish()
    }

    private fun createDialog() {
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

    private fun displayErrorMessage(messageReference: Int) {
        tvError.text = resources.getString(messageReference)
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
    }
}
