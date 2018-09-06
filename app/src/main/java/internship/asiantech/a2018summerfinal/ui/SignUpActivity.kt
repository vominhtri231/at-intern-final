package internship.asiantech.a2018summerfinal.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


@Suppress("DEPRECATED_IDENTITY_EQUALS", "UNUSED_EXPRESSION")
class SignUpActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private var avatar: String = ""
    private var age: Int = 0
    private var location: LatLng = LatLng(16.0544, 108.2022)
    private val auth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance()

    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_GALLERY = 2
        private const val URL_IMAGE = "gs://a2018summerfinal-76c35.appspot.com"
        const val MAIL_KEY = "mail"
    }

    object UserValidate{
        fun mailValidate(mail: String): Boolean {
            val validateMail =
                    Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9._%+-]*+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
            val matcher = validateMail.matcher(mail)
            return matcher.find()
        }

        fun nameValidate(name: String): Boolean {
            val validatePhone =
                    Pattern.compile("[a-zA-Z]{2,40}$", Pattern.CASE_INSENSITIVE)
            val matcher = validatePhone.matcher(name)
            return matcher.find()
        }

        fun passwordValidate(password: String): Boolean {
            val validatePhone =
                    Pattern.compile("[a-zA-Z0-9]{6,}", Pattern.CASE_INSENSITIVE)
            val matcher = validatePhone.matcher(password)
            return matcher.find()
        }

        fun ageValidate(age: Int): Boolean {
            return age in 10..100
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        btnSignUp.setOnClickListener {
           val mail = edtMail.text.toString()
           val name = edtName.text.toString()
           val password = edtPassword.text.toString()
           val repeatPassword = edtRepeatPassword.text.toString()
            val ageString = edtAge.text.toString()
            if (checkUser(mail, name, password, repeatPassword, ageString)) {
                signUp(mail, password, name)
            }
        }
        imgAvatar.setOnClickListener {
            createDialog()
        }
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

    private fun signUp(mail: String, password: String, name: String) {
        auth?.createUserWithEmailAndPassword(mail, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        insertUser(mail, name, password)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra(MAIL_KEY, mail)
                        setResult(LoginActivity.REQUEST_CODE, intent)
                        finish()
                    } else {
                        tvError.text = resources.getString(R.string.error_sign_up)
                        tvError.setBackgroundResource(R.drawable.border_text_view_error)
                    }
                }
    }

    private fun insertUser(mail: String, name: String, password: String) {
        uploadImage()
        var idUser = ""
        database.child("Users").push().key?.let {
            idUser = it
        }
        val user = User(idUser, mail, name, password, age, avatar, location.latitude, location.longitude)
        database.child("Users").child(idUser).setValue(user)
    }

    private fun checkUser(mail: String, name: String, password: String, repeatPassword: String, ageString: String): Boolean {
        if (ageString == "" || mail == "" || name == "" || password == "" || repeatPassword == "") {
            tvError.text = resources.getString(R.string.error_not_enough_information)
            tvError.setBackgroundResource(R.drawable.border_text_view_error)
            return false
        }
        if (password != repeatPassword) {
            tvError.text = resources.getString(R.string.error_password_not_match_repeat_password)
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
        if (!UserValidate.mailValidate(mail)) {
            tvError.text = resources.getString(R.string.error_mail)
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

    private fun uploadImage() {
        val storageRef = storage.getReferenceFromUrl(URL_IMAGE.trim())
        val calendar = Calendar.getInstance()
        val mountainsRef = storageRef.child("${calendar.timeInMillis}.png")
        imgAvatar.isDrawingCacheEnabled = true
        imgAvatar.buildDrawingCache()
        val bitmap = (imgAvatar.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, resources.getString(R.string.error_upload_image), Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { p0 ->
            p0?.uploadSessionUri?.let {
                avatar = it.toString()
            }
        }
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
