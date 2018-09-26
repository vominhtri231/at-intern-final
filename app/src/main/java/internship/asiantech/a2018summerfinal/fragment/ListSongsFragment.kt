package internship.asiantech.a2018summerfinal.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.MusicAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.SongUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import kotlinx.android.synthetic.main.fragment_list_songs.*

class ListSongsFragment : Fragment() {
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var musics: MutableList<Song>

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
            musicAdapter = MusicAdapter(musics, context) { position ->
                musics[position].isFavourite = !musics[position].isFavourite

                val song = Song(musics[position].id, musics[position].title, musics[position].artist, musics[position].duration, musics[position].isFavourite)
                AppDataHelper.getInstance(context).addSong(song)
                if (musics[position].isFavourite) {
                    addPlayListToDataBase(resources.getString(R.string.favourite))
                    AppDataHelper.getInstance(context).addSongToPlaylist(resources.getString(R.string.favourite), song.id)
                } else {
                    AppDataHelper.getInstance(context).deleteSongInPlaylist(resources.getString(R.string.favourite), song.id)
                }
                musicAdapter.notifyDataSetChanged()
            }

            recyclerViewMusic.adapter = musicAdapter
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
        musics = ArrayList()
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.history), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        musics.add(0, Song(song.id, song.title, song.artist, song.duration, song.isFavourite))
                    }
                    musicAdapter.notifyDataSetChanged()
                }
            })
        }
        checkIsFavourite()
    }

    private fun getFavouriteList() {
        musics = ArrayList()
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.history), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        musics.add(Song(song.id, song.title, song.artist, song.duration, song.isFavourite))
                    }
                }
            })
        }
        checkIsFavourite()
    }

    private fun getAllList() {
        context?.let {
            val listMusic = ListMusic(it)
            musics.clear()
            musics.addAll(listMusic.getListMusics())
        }
        checkIsFavourite()
    }

    private fun checkIsFavourite() {
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(resources.getString(R.string.favourite), object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    for (song in result) {
                        for (i in 0 until musics.size) {
                            if (song.id == musics[i].id) {
                                musics[i].isFavourite = true
                                musicAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
        }
    }
}
