package internship.asiantech.a2018summerfinal.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.CommonUpdater
import internship.asiantech.a2018summerfinal.model.Album
import internship.asiantech.a2018summerfinal.ui.dialog.listener.AddPlaylistEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.ListSongFragment
import internship.asiantech.a2018summerfinal.ui.fragment.SearchSongFragment
import internship.asiantech.a2018summerfinal.ui.fragment.StandardFragment
import internship.asiantech.a2018summerfinal.ui.fragment.listener.BackEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LibraryEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.StandardEventListener
import internship.asiantech.a2018summerfinal.utils.askForPermissions
import internship.asiantech.a2018summerfinal.utils.queryAlbum
import internship.asiantech.a2018summerfinal.utils.querySongs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
        , StandardEventListener, BackEventListener, AddPlaylistEventListener, LibraryEventListener {
    val songs = mutableListOf<Song>()
    val albums = mutableListOf<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initDrawerLayout()
        setFirstFragment()
    }

    private fun initDrawerLayout() {
        //TODO : init recycler view that contain user's information
    }

    private fun initData() {
        if (askForPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)) {
            songs.addAll(querySongs(contentResolver))
            albums.addAll(queryAlbum(contentResolver))
        }
    }

    private fun setFirstFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, StandardFragment()).commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (isResultGranted(grantResults)) {
                songs.addAll(querySongs(contentResolver))
            }
        }
    }

    private fun isResultGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun onStartSearch() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, SearchSongFragment()).commit()
    }

    override fun onViewUserInfo() {
        drawerLayout.openDrawer(Gravity.END)
    }

    override fun onBackToStandard() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, StandardFragment()).commit()
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

    override fun openHistorySong() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, ListSongFragment.instance(ListSongFragment.TYPE_HISTORY)).commit()
    }

    override fun openFavoriteSong() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, ListSongFragment.instance(ListSongFragment.TYPE_FAVORITE)).commit()
    }

    companion object {
        private val REQUEST_CODE = 1583
    }
}
