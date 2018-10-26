package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.firebase.FirebaseDatabaseUtils
import internship.asiantech.a2018summerfinal.firebase.FirebaseStorageUtils
import internship.asiantech.a2018summerfinal.firebase.GetDownloadUpdater
import internship.asiantech.a2018summerfinal.firebase.StorageUpdater
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.sharepreference.UserSharePreference
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.dialog.ChooseImageDialog
import internship.asiantech.a2018summerfinal.ui.fragment.listener.ProfileFragmentListener
import internship.asiantech.a2018summerfinal.utils.checkUserUpdate
import internship.asiantech.a2018summerfinal.utils.getBitmapFromImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException


class ProfileFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private lateinit var map: GoogleMap
    private lateinit var location: LatLng
    private lateinit var userSharedPreferences: UserSharePreference
    private var mode: Mode = Mode.ViewMode
    private var isChangeImage = false
    private lateinit var listener: ProfileFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(USER_KEY)?.let { userString ->
            Gson().fromJson(userString, User::class.java)?.let {
                location = LatLng(it.latitude, it.longitude)
                initView(it)
                initMap()
                initListener()
            }
        }
    }

    private fun initListener() {
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
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun initView(currentUser: User) {
        edtMail.setText(currentUser.mail)
        edtName.setText(currentUser.name)
        edtAge.setText(currentUser.age.toString())
        Log.e(TAG, currentUser.avatarPath)
        if (currentUser.avatarPath.isNotEmpty()) {
            FirebaseStorageUtils.getDownloadUrl(currentUser.avatarPath, object : GetDownloadUpdater {
                override fun handle(downloadUrl: String) {
                    Glide.with(this@ProfileFragment).load(downloadUrl).into(imgAvatar)
                }
            })
        }
    }

    private fun changeToEditMode() {
        mode = Mode.EditMode
        edtName.isEnabled = true
        edtAge.isEnabled = true
        btnEdit.text = resources.getString(R.string.action_change)
        imgAvatar.setOnClickListener {
            createDialog()
        }
    }

    private fun changeToViewMode() {
        mode = Mode.ViewMode
        edtName.isEnabled = false
        edtAge.isEnabled = false
        btnEdit.text = resources.getString(R.string.action_edit)
        imgAvatar.setOnClickListener {}
    }

    private fun createDialog() {
        ChooseImageDialog().show(childFragmentManager, ChooseImageDialog.NAME)
    }

    fun setDataToImageView(requestCode: Int, data: Intent) {
        when (requestCode) {
            MainActivity.PICK_FROM_CAMERA_REQUEST_CODE -> {
                if (data.extras != null) {
                    val bitmap = data.extras?.get("data") as Bitmap
                    imgAvatar.setImageBitmap(bitmap)
                    isChangeImage = true
                }
            }
            MainActivity.PICK_FROM_GALLERY_REQUEST_CODE -> {
                val uri = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    imgAvatar.setImageBitmap(bitmap)
                    isChangeImage = true
                } catch (e: IOException) {
                    displayErrorMessage(R.string.error_load_gallery)
                }
            }
        }
    }

    private fun updateUser() {
        val name = edtName.text.toString()
        val ageString = edtAge.text.toString()
        val validateResult = checkUserUpdate(name, ageString)

        if (validateResult.isSuccess()) {
            val user = userSharedPreferences.getCurrentUser()
            user?.let {
                it.name = name
                it.age = ageString.toInt()
                updateUserImageToDatabase(it.idUser)
                FirebaseDatabaseUtils.updateUserInfo(it.idUser, it)
                userSharedPreferences.saveUserLogin(it)
            }
        } else {
            displayErrorMessage(validateResult.getMessage())
        }
    }

    private fun updateUserImageToDatabase(idUser: String) {
        if (!isChangeImage) return
        FirebaseStorageUtils.uploadImage(
                getBitmapFromImageView(imgAvatar), object : StorageUpdater {
            override fun onFail() {
                displayErrorMessage(R.string.error_upload_image)
            }

            override fun onSuccess(path: String) {
                Log.e(TAG, "Path:$path")
                userSharedPreferences.changeAvatar(path)
                FirebaseDatabaseUtils.updateImageReference(idUser, path)
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


    companion object {
        private val TAG = ProfileFragment::class.qualifiedName
        private const val USER_KEY = "USER_KEY"
        @JvmStatic
        fun newInstance(user: User) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        this.putString(USER_KEY, Gson().toJson(user))
                    }
                }
    }

    enum class Mode {
        ViewMode,
        EditMode;
    }
}
