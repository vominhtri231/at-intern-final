package internship.asiantech.a2018summerfinal.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import internship.asiantech.a2018summerfinal.ChoiceSongActivity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.ListSongInPlaylistAdapter
import internship.asiantech.a2018summerfinal.adapter.PlaylistAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.SongUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.EventClickItemSongListener
import internship.asiantech.a2018summerfinal.fragment.PlaylistFragment
import kotlinx.android.synthetic.main.fragment_list_songs_in_playlist.*
import kotlinx.android.synthetic.main.fragment_playlist.*

class ListSongInPlaylistActivity : AppCompatActivity(), EventClickItemSongListener {
    private var listSongInPlaylist: MutableList<Song> = mutableListOf()
    private lateinit var mListSongInPlaylistAdapter: ListSongInPlaylistAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerviewListSongInPlaylist: RecyclerView
    private var nameIdPlayList: String = ""
    private lateinit var dataHelper: AppDataHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_songs_in_playlist)

        //get listsong listSongInPlaylist
        dataHelper = AppDataHelper.getInstance(this)

        initViews()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        nameIdPlayList = intent.getStringExtra(PlaylistFragment.KEY_ID_PLAYLIST_BUNDLE)
        applicationContext?.let {
            AppDataHelper.getInstance(applicationContext).getSongInPlaylist(nameIdPlayList, object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    listSongInPlaylist.clear()
                    for (song in result) {
                        listSongInPlaylist.add(song)
                    }
                    mListSongInPlaylistAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun setListener() {
        tvAddSongToPlaylist.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ChoiceSongActivity::class.java)
            intent.putExtra(PlaylistFragment.KEY_ID_PLAYLIST_BUNDLE, nameIdPlayList)
            startActivity(intent)
        })
        tvChangeSongInPlaylist.setOnClickListener(View.OnClickListener {

        })
        btnToolBarListSongInPlaylistClose.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }

    private fun initViews() {

        mRecyclerviewListSongInPlaylist = findViewById(R.id.recyclerviewListSongInPlaylist)
        mLayoutManager = LinearLayoutManager(this)
        mListSongInPlaylistAdapter = ListSongInPlaylistAdapter(listSongInPlaylist
                , this, this)
        mRecyclerviewListSongInPlaylist.layoutManager = mLayoutManager
        mRecyclerviewListSongInPlaylist.setHasFixedSize(true)
        mRecyclerviewListSongInPlaylist.adapter = mListSongInPlaylistAdapter

    }

    override fun deleteSongOnClick(position: Int) {
        dataHelper.deleteSongInPlaylist(nameIdPlayList, listSongInPlaylist.get(position).id)
        listSongInPlaylist.removeAt(position)
        mListSongInPlaylistAdapter.notifyDataSetChanged()
    }
}
