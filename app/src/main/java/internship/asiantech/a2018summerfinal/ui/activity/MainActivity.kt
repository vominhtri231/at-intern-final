package internship.asiantech.a2018summerfinal.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.DataController
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.CommonUpdater
import internship.asiantech.a2018summerfinal.firebase.AuthUpdater
import internship.asiantech.a2018summerfinal.firebase.DatabaseUpdater
import internship.asiantech.a2018summerfinal.firebase.FirebaseAuthUtils
import internship.asiantech.a2018summerfinal.firebase.FirebaseDatabaseUtils
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.sharepreference.UserSharePreference
import internship.asiantech.a2018summerfinal.ui.dialog.ChooseImageDialog
import internship.asiantech.a2018summerfinal.ui.dialog.listener.AddPlaylistEventListener
import internship.asiantech.a2018summerfinal.ui.dialog.listener.ChooseImageEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.*
import internship.asiantech.a2018summerfinal.ui.fragment.listener.*
import internship.asiantech.a2018summerfinal.utils.askForPermissions

class MainActivity : AppCompatActivity(), StandardFragmentActionListener,
        ListSongFragmentActionListener, AddPlaylistEventListener, LibraryFragmentActionListener,
        AdditionFragmentActionListener, LoginFragmentListener, SignUpFragmentListener,
        ChooseImageEventListener {

    private var dataController: DataController? = null
    private lateinit var userSharedPreferences: UserSharePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userSharedPreferences = UserSharePreference(this)
        initData()
        setFirstFragment()
        checkAdditionFragment()
    }

    private fun initData() {
        if (askForPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)) {
            dataController = DataController(this)
        }
    }

    fun getSongs(): List<Song> {
        dataController?.let {
            return it.songs
        }
        return listOf()
    }

    private fun setFirstFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, StandardFragment())
                .commit()
    }

    private fun checkAdditionFragment() {
        intent.getStringExtra(FRAGMENT_NAME_KEY).let {
            if (it == PlayMusicFragment::class.qualifiedName) {
                val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
                if (fragment !is PlayMusicFragment) {
                    replaceMainFragment(PlayMusicFragment())
                }
            }
        }
    }

    override fun onStartSearch() = replaceMainFragment(SearchSongFragment())

    override fun onViewUserInfo() {
        if (userSharedPreferences.isLogin()) {
            openProfileFragment()
        } else {
            replaceMainFragment(LoginFragment())
        }
    }

    override fun onBackToStandard() {
        supportFragmentManager.popBackStackImmediate()
    }

    override fun addPlaylist(name: String) {
        AppDataHelper.getInstance(this)
                .addPlaylist(Playlist(name = name), object : CommonUpdater {
                    override fun onFinish() {
                        val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
                        if (fragment is StandardFragment) {
                            fragment.updatePlaylistFragment()
                        }
                    }
                })
    }

    override fun openHistorySong() =
            replaceMainFragment(ListSongFragment.instance(ListSongFragment.TYPE_HISTORY))

    override fun openFavoriteSong() =
            replaceMainFragment(ListSongFragment.instance(ListSongFragment.TYPE_FAVORITE))

    override fun openAllSong() =
            replaceMainFragment(ListSongFragment.instance(ListSongFragment.TYPE_ALL))

    override fun onStartPlay(songId: Long) =
            replaceMainFragment(PlayMusicFragment.newInstance(songId))


    override fun onFavoriteChange(songId: Long) {
        dataController?.changeFavoriteState(songId)
        val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
        if (fragment is ListSongFragment) {
            fragment.getSongs()
        }
    }

    override fun onStartSignUp() {
        replaceMainFragment(SignUpFragment())
    }

    override fun onLogin(mail: String, password: String) {
        FirebaseAuthUtils.login(mail, password, this, object : AuthUpdater {
            override fun onSuccess() {
                saveCurrentUser(mail)
                openProfileFragment()
            }

            override fun onFail() {
                val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
                if (fragment is LoginFragment) {
                    fragment.displayErrorMessage(R.string.error_login)
                }
            }
        })
    }


    override fun onSignUp(user: User) {
        FirebaseAuthUtils.createUser(user.mail, user.password, this, object : AuthUpdater {
            override fun onSuccess() {
                FirebaseDatabaseUtils.addUser(user)
                replaceMainFragment(LoginFragment.newInstance(user.mail))
            }

            override fun onFail() {
                val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
                if (fragment is SignUpFragment) {
                    fragment.displayErrorMessage(R.string.error_sign_up)
                }
            }
        })
    }

    override fun open(type: ChooseImageDialog.ChosenImageType) {
        when (type) {
            ChooseImageDialog.ChosenImageType.CameraType -> {
                if (askForPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                PICK_FROM_CAMERA_REQUEST_CODE)) {
                    openCamera()
                }
            }
            ChooseImageDialog.ChosenImageType.GalleryType -> {
                if (askForPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                PICK_FROM_GALLERY_REQUEST_CODE)) {
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
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        if (isResultGranted(grantResults)) {
            when (requestCode) {
                PICK_FROM_CAMERA_REQUEST_CODE -> openCamera()
                PICK_FROM_GALLERY_REQUEST_CODE -> openGallery()
                REQUEST_CODE -> dataController = DataController(this)
            }
        }
    }

    private fun isResultGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
            if (fragment is ProfileFragment) {
                fragment.setDataToImageView(requestCode, data)
            }
        }
    }

    private fun replaceMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun saveCurrentUser(mail: String) {
        FirebaseDatabaseUtils.getCurrentUser(mail, object : DatabaseUpdater {
            override fun onComplete(user: User) {
                userSharedPreferences.saveUserLogin(user)
            }
        })
    }

    private fun openProfileFragment() {
        userSharedPreferences.getCurrentUser()?.let {
            replaceMainFragment(ProfileFragment.newInstance(it))
        }
    }

    companion object {
        const val PICK_FROM_CAMERA_REQUEST_CODE = 1
        const val PICK_FROM_GALLERY_REQUEST_CODE = 2
        private const val REQUEST_CODE = 1583
        const val FRAGMENT_NAME_KEY = "FRAGMENT_NAME_KEY"
    }
}
