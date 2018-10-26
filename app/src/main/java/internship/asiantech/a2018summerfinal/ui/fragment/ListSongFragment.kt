package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.SongUpdater
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.fragment.listener.ListSongFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.SongAdapter
import kotlinx.android.synthetic.main.addition_recylerview.*
import kotlinx.android.synthetic.main.fragment_addition.*
import kotlinx.android.synthetic.main.addtion_list_song_header.*

class ListSongFragment : AdditionFragment() {
    private lateinit var listSongFragmentListener: ListSongFragmentActionListener
    private lateinit var playlistName: String
    private val songs: MutableList<Song> = mutableListOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ListSongFragmentActionListener) {
            listSongFragmentListener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement ListSongFragmentActionListener")
        }
    }

    override fun initView() {
        playlistName = getPlaylistName(arguments)
        tvName.text = playlistName
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SongAdapter(songs, context, listSongFragmentListener)
        getSongs()
    }

    override fun addDiffViews(inflater: LayoutInflater) {
        inflater.inflate(R.layout.addtion_list_song_header, flDiff)
        inflater.inflate(R.layout.addition_recylerview, flAdditionContent)
    }

    private fun getPlaylistName(bundle: Bundle?): String {
        val type = bundle?.getInt(KEY_TYPE, 0)
        return when (type) {
            TYPE_HISTORY -> resources.getString(R.string.history)
            TYPE_FAVORITE -> resources.getString(R.string.favourite)
            else -> resources.getString(R.string.all_song)
        }
    }

    fun getSongs() {
        songs.clear()
        if (playlistName == resources.getString(R.string.all_song)) {
            getAllSong()
        } else {
            getSongsFromPlayList(playlistName)
        }
    }

    private fun getSongsFromPlayList(playlistName: String) {
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(playlistName, object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    songs.addAll(result)
                    recyclerView.adapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun getAllSong() {
        songs.addAll((activity as MainActivity).getSongs())
        recyclerView.adapter.notifyDataSetChanged()
    }

    companion object {
        private const val KEY_TYPE = "KEY_TYPE"
        const val TYPE_ALL = 0
        const val TYPE_HISTORY = 1
        const val TYPE_FAVORITE = 2

        fun instance(@ListSongFragmentType type: Int) =
                ListSongFragment().apply {
                    arguments = Bundle().apply {
                        this.putInt(KEY_TYPE, type)
                    }
                }
    }

    @IntDef(TYPE_HISTORY, TYPE_FAVORITE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ListSongFragmentType
}
