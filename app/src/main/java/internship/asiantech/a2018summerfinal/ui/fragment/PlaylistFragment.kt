package internship.asiantech.a2018summerfinal.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.updater.PlaylistUpdater
import internship.asiantech.a2018summerfinal.ui.activity.ListSongInPlaylistActivity
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.PlaylistAdapter
import internship.asiantech.a2018summerfinal.ui.dialog.AddPlaylistDialog
import internship.asiantech.a2018summerfinal.ui.recyclerview.listener.PlaylistViewHolderListener
import kotlinx.android.synthetic.main.fragment_playlist.*

class PlaylistFragment : Fragment(), PlaylistViewHolderListener {
    private var playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getPlayList()
    }

    fun getPlayList() {
        context?.let {
            AppDataHelper.getInstance(it).getAllPlaylist(object : PlaylistUpdater {
                override fun getPlaylistResult(result: List<Playlist>) {
                    playlists.clear()
                    playlists.addAll(result)
                    recyclerViewPlaylist.adapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun initViews() {
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(activity)
        recyclerViewPlaylist.adapter = PlaylistAdapter(playlists, context, this)
        imgAdd.setOnClickListener{
            AddPlaylistDialog().show(activity?.supportFragmentManager,AddPlaylistDialog.NAME)
        }
    }

    override fun addObjectOnClick(position: Int) {
        val namePlaylist = playlists[position].name
        val intent = Intent(context, ListSongInPlaylistActivity::class.java)
        intent.putExtra(KEY_ID_PLAYLIST_BUNDLE, namePlaylist)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLAYLIST_REQUEST_CODE && resultCode == RESULT_OK) {
            recyclerViewPlaylist.adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val ADD_PLAYLIST_REQUEST_CODE = 200
        const val KEY_ID_PLAYLIST_BUNDLE = "keyIdNamePlaylist"
    }
}
