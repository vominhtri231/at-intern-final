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
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.sharepreference.UserSharePreference
import internship.asiantech.a2018summerfinal.utils.checkUserUpdate
import kotlinx.android.synthetic.main.activity_profile_user.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class ProfileUserActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private lateinit var user: User
    private lateinit var location: LatLng
    private lateinit var avatar: String
    private var age: Int = 0
    private var isSave = false
    private val storage = FirebaseStorage.getInstance()
    private lateinit var userSharedPreferences: UserSharePreference

    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_GALLERY = 2
        private const val URL_IMAGE = "gs://a2018summerfinal-76c35.appspot.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        userSharedPreferences = UserSharePreference(this)
        initMap()
        initUser()
        btnEdit.setOnClickListener { _ ->
            if (!isSave) {
                edtNewPassword.visibility = View.VISIBLE
                edtRepeatPassword.visibility = View.VISIBLE
                edtName.isEnabled = true
                edtAge.isEnabled = true
                edtPassword.isEnabled = true
                edtPassword.setText("")
                edtPassword.hint = resources.getString(R.string.prompt_old_password)
                btnEdit.text = resources.getString(R.string.action_change)
                imgAvatar.setOnClickListener {
                    createDialog()
                }
            } else {
                updateUSer()
            }
            isSave = !isSave
        }
    }

    private fun updateUSer() {
        val database = FirebaseDatabase.getInstance().reference
        val name = edtName.text.toString()
        val oldPassword = edtPassword.text.toString()
        val newPassword = edtNewPassword.text.toString()
        val repeatPassword = edtRepeatPassword.text.toString()
        val ageString = edtAge.text.toString()

        val result = checkUserUpdate(name, oldPassword, newPassword, repeatPassword, ageString)

        if (result.isSuccess()) {
            resetPassword(newPassword)
            uploadImage()
            val userUpdate = User(idUser = user.idUser, mail = user.mail, name = name,
                    password = newPassword, age = age, avatar = avatar,
                    latitude = location.latitude, longitude = location.longitude)
            database.child("Users").child(user.idUser).setValue(userUpdate)
            userSharedPreferences.removeUserCurrent()
            userSharedPreferences.saveUserLogin(user)
            btnEdit.text = resources.getString(R.string.action_edit)
            edtName.isEnabled = false
            edtAge.isEnabled = false
            edtPassword.isEnabled = false
            edtNewPassword.visibility = View.GONE
            edtRepeatPassword.visibility = View.GONE
            edtPassword.text = Editable.Factory.getInstance().newEditable(newPassword)
        } else {
            isSave = false
        }
    }

    private fun resetPassword(newPassword: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.updatePassword(newPassword)
                ?.addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        toastError(resources.getString(R.string.error_reset_password))
                    }
                }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun toastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    private fun initUser() {
        val tempUser = userSharedPreferences.getCurrentUser()
        tempUser?.let {
            user = it
            avatar = it.avatar
            location = LatLng(it.latitude, it.longitude)
            edtMail.text = Editable.Factory.getInstance().newEditable(it.mail)
            edtName.text = Editable.Factory.getInstance().newEditable(it.name)
            edtAge.text = Editable.Factory.getInstance().newEditable(it.age.toString())
            edtPassword.text = Editable.Factory.getInstance().newEditable(it.password)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val location = LatLng(user.latitude, user.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
        map.uiSettings.isZoomControlsEnabled = true
        map.addMarker(MarkerOptions().position(location)
                .title(resources.getString(R.string.your_location)))
        map.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng?) {
        p0?.let {
            location = it
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun uploadImage() {
        if (avatar != user.avatar) {
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
                toastError(resources.getString(R.string.error_upload_image))
            }.addOnSuccessListener { p0 ->
                p0?.uploadSessionUri?.let {
                    avatar = it.toString()
                }
            }
        }
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
                    toastError(resources.getString(R.string.error_load_gallery))
                }
            }
        }
    }
}
