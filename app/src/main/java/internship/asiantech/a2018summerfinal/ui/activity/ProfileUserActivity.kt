package internship.asiantech.a2018summerfinal.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.firebase.FirebaseDatabaseUtils
import internship.asiantech.a2018summerfinal.firebase.FirebaseStorageUtils
import internship.asiantech.a2018summerfinal.firebase.StorageUpdater
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.sharepreference.UserSharePreference
import internship.asiantech.a2018summerfinal.ui.dialog.ChooseImageDialog
import internship.asiantech.a2018summerfinal.utils.askForPermissions
import internship.asiantech.a2018summerfinal.utils.checkUserUpdate
import internship.asiantech.a2018summerfinal.utils.getBitmapFromImageView
import kotlinx.android.synthetic.main.activity_profile_user.*
import java.io.IOException

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class ProfileUserActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener
        , ChooseImageDialog.ChooseImageListener {

    private lateinit var map: GoogleMap
    private lateinit var location: LatLng
    private lateinit var userSharedPreferences: UserSharePreference
    private var mode: Mode = Mode.ViewMode

    companion object {
        private const val PICK_FROM_CAMERA_REQUEST_CODE = 1
        private const val PICK_FROM_GALLERY_REQUEST_CODE = 2
        private val TAG = ProfileUserActivity::class.qualifiedName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSharedPreferences = UserSharePreference(this)
        val user = userSharedPreferences.getCurrentUser()
        if (user != null) {
            location = LatLng(user.latitude, user.longitude)
            setContentView(R.layout.activity_profile_user)
            initView(user)
            initMap()
            initListeners()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initListeners() {
        imgAvatar.setOnClickListener {
            createDialog()
        }
        btnEdit.setOnClickListener { _ ->
            if (mode == Mode.ViewMode) {
                changeToEditMode()
            } else {
                changeToViewMode()
                updateUser()
            }
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun initView(currentUser: User) {
        edtMail.setText(currentUser.mail)
        edtName.setText(currentUser.name)
        edtAge.setText(currentUser.age.toString())
        if (currentUser.avatar.isNotEmpty()) {
            Glide.with(this).load(currentUser.avatar).into(imgAvatar)
        }
    }

    private fun changeToEditMode() {
        mode = Mode.EditMode
        edtName.isEnabled = true
        edtAge.isEnabled = true
        btnEdit.text = resources.getString(R.string.action_change)
    }

    private fun changeToViewMode() {
        mode = Mode.ViewMode
        edtName.isEnabled = false
        edtAge.isEnabled = false
        btnEdit.text = resources.getString(R.string.action_edit)
    }

    private fun createDialog() {
        ChooseImageDialog().show(supportFragmentManager, "choose image")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            setDataToImage(requestCode, data)
        }
    }

    private fun setDataToImage(requestCode: Int, data: Intent) {
        when (requestCode) {
            PICK_FROM_CAMERA_REQUEST_CODE -> {
                if (data.extras != null) {
                    val bitmap = data.extras?.get("data") as Bitmap
                    imgAvatar.setImageBitmap(bitmap)
                }
            }
            PICK_FROM_GALLERY_REQUEST_CODE -> {
                val uri = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imgAvatar.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    displayErrorMessage(R.string.error_load_gallery)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PICK_FROM_CAMERA_REQUEST_CODE -> {
                if (isResultGranted(grantResults)) {
                    openCamera()
                } else {
                    displayErrorMessage(R.string.error_open_camera)
                }
                return
            }
            PICK_FROM_GALLERY_REQUEST_CODE -> {
                if (isResultGranted(grantResults)) {
                    openGallery()
                } else {
                    displayErrorMessage(R.string.error_open_galery)
                }
                return
            }
        }
    }

    private fun isResultGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun open(type: ChooseImageDialog.ChosenImageType) {
        when (type) {
            ChooseImageDialog.ChosenImageType.CameraType -> {
                if (askForPermissions(this, arrayOf(Manifest.permission.CAMERA), PICK_FROM_CAMERA_REQUEST_CODE)) {
                    openCamera()
                }
            }
            ChooseImageDialog.ChosenImageType.GalleryType -> {
                if (askForPermissions(this, arrayOf(Manifest.permission.CAMERA), PICK_FROM_GALLERY_REQUEST_CODE)) {
                    openGallery()
                }
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, PICK_FROM_CAMERA_REQUEST_CODE)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY_REQUEST_CODE)
    }

    private fun updateUser() {
        val name = edtName.text.toString()
        val ageString = edtAge.text.toString()
        val result = checkUserUpdate(name, ageString)

        if (result.isSuccess()) {
            val user = userSharedPreferences.getCurrentUser()
            user?.let {
                it.name = name
                it.age = ageString.toInt()
                updateUserImageToDatabase(it.idUser)
                FirebaseDatabaseUtils.updateUserInfo(it.idUser, it)
                userSharedPreferences.removeUserCurrent()
                userSharedPreferences.saveUserLogin(it)
            }
        } else {
            displayErrorMessage(result.getMessage())
        }
    }

    private fun updateUserImageToDatabase(idUser: String) {
        FirebaseStorageUtils.uploadImage(getBitmapFromImageView(imgAvatar), object : StorageUpdater {
            override fun onSuccess(uri: Uri) {
                Log.e(TAG, uri.toString())
                FirebaseDatabaseUtils.updateImageReference(idUser, uri.toString())
            }

            override fun onFail() {
                displayErrorMessage(R.string.error_upload_image)
            }
        })
    }

    private fun displayErrorMessage(messageReference: Int) {
        tvError.text = resources.getString(messageReference)
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
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

    enum class Mode {
        ViewMode,
        EditMode;
    }
}
