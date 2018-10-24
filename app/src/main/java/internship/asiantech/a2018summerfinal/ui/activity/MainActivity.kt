package internship.asiantech.a2018summerfinal.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.DataController
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.CommonUpdater
import internship.asiantech.a2018summerfinal.ui.dialog.listener.AddPlaylistEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.ListSongFragment
import internship.asiantech.a2018summerfinal.ui.fragment.PlayMusicFragment
import internship.asiantech.a2018summerfinal.ui.fragment.SearchSongFragment
import internship.asiantech.a2018summerfinal.ui.fragment.StandardFragment
import internship.asiantech.a2018summerfinal.ui.fragment.listener.AdditionFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LibraryFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.ListSongFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.StandardFragmentActionListener
import internship.asiantech.a2018summerfinal.utils.askForPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
        , StandardFragmentActionListener, ListSongFragmentActionListener, AddPlaylistEventListener,
        LibraryFragmentActionListener, AdditionFragmentActionListener {


    var dataController:DataController? = null

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
            dataController = DataController(this)
        }
    }

    fun getSongs():List<Song>{
        dataController?.let{
            return it.songs
        }
        return listOf()
    }

    private fun setFirstFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, StandardFragment()).commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (isResultGranted(grantResults)) {
                dataController = DataController(this)
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
                .add(R.id.flMain, ListSongFragment.instance(ListSongFragment.TYPE_HISTORY)).commit()
    }

    override fun openFavoriteSong() {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, ListSongFragment.instance(ListSongFragment.TYPE_FAVORITE))
                .commit()
    }

    override fun openAllSong() {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, ListSongFragment.instance(ListSongFragment.TYPE_ALL))
                .commit()
    }

    override fun onStartPlay(songId: Long) {
        supportFragmentManager.beginTransaction()
                .add(R.id.flMain, PlayMusicFragment())
                .commit()
    }

    override fun onFavoriteChange(songId: Long) {
        dataController?.changeFavoriteState(songId)
        val fragment = supportFragmentManager.findFragmentById(R.id.flMain)
        if (fragment is ListSongFragment) {
            fragment.changeListSongView()
        }
    }

    companion object {
        private const val REQUEST_CODE = 1583
    }
}
