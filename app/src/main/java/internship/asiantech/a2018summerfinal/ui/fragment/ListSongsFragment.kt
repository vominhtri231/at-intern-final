package internship.asiantech.a2018summerfinal.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.adapter.SongAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.SongUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import internship.asiantech.a2018summerfinal.ui.viewholder.SongViewHolderListener
import kotlinx.android.synthetic.main.fragment_list_songs.*

class ListSongsFragment : Fragment() {
    private lateinit var songAdapter: SongAdapter
    private val songs: MutableList<Song> = mutableListOf()

    companion object {
        private const val KEY_POSITION = "position"
        fun instance(position: Int): ListSongsFragment {
            val timelineFragment = ListSongsFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_POSITION, position)
            timelineFragment.arguments = bundle
            return timelineFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_songs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListMusic()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        context?.let { context ->
            val layoutManager = LinearLayoutManager(context)
            recyclerViewMusic.layoutManager = layoutManager
            songAdapter = SongAdapter(songs, context, object : SongViewHolderListener {
                override fun onFavoriteChange(position: Int) {
                    songs[position].isFavourite = !songs[position].isFavourite
                    val song = songs[position]
                    AppDataHelper.getInstance(context).addSong(song)
                    if (songs[position].isFavourite) {
                        addPlayListToDataBase(resources.getString(R.string.favourite))
                        AppDataHelper.getInstance(context).addSongToPlaylist(resources.getString(R.string.favourite), song.id)
                    } else {
                        AppDataHelper.getInstance(context).deleteSongInPlaylist(resources.getString(R.string.favourite), song.id)
                    }
                    songAdapter.notifyDataSetChanged()
                    recyclerViewMusic.adapter = songAdapter
                }

                override fun onStartListen(position: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }

    private fun addPlayListToDataBase(name: String) {
        context?.let {
            AppDataHelper.getInstance(it).getAllPlaylist(object : PlaylistUpdater {
                override fun getPlaylistResult(result: List<Playlist>) {
                    if (!result.contains(Playlist(name))) {
                        AppDataHelper.getInstance(it).addPlaylist(Playlist(name))
                    }
                }

            })
        }
    }

    private fun initListMusic() {
        val bundle = arguments
        var position = 0
        if (bundle != null) {
            position = bundle.getInt(KEY_POSITION)
        }
        when (position) {
            0 -> {
                getAllList()
            }
            1 -> {
                getFavouriteList()
            }
            2 -> {
                getHistoryList()
            }
        }
    }

    private fun getHistoryList() {
        songs.clear()
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.history), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        songs.add(0, Song(song.id, song.title, song.artist, song.duration, song.isFavourite))
                    }
                    songAdapter.notifyDataSetChanged()
                }
            })
        }
        checkIsFavourite()
    }

    private fun getFavouriteList() {
        songs.clear()
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.history), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        songs.add(Song(song.id, song.title, song.artist, song.duration, song.isFavourite))
                    }
                }
            })
        }
        checkIsFavourite()
    }

    private fun getAllList() {
        context?.let {
            val listMusic = ListMusic(it)
            songs.clear()
            songs.addAll(listMusic.getListMusics())
        }
        checkIsFavourite()
    }

    private fun checkIsFavourite() {
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.favourite), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        for (i in 0 until songs.size) {
                            if (song.id == songs[i].id) {
                                songs[i].isFavourite = true
                                songAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
        }
    }
}
