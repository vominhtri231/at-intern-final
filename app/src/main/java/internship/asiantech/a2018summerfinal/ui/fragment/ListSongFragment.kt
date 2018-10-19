package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.SongUpdater
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.fragment.listener.BackEventListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.SongAdapter
import internship.asiantech.a2018summerfinal.ui.recyclerview.listener.SongViewHolderListener
import kotlinx.android.synthetic.main.fragment_list_song.*

class ListSongFragment : Fragment() {
    private lateinit var songAdapter: SongAdapter
    private lateinit var listener: BackEventListener
    private val songs: MutableList<Song> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        initRecyclerView()

        val listSongName = getFragmentName(arguments)
        initSongs(listSongName)
        initView(listSongName)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BackEventListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement BackEventListener")
        }
    }

    private fun setListener(){
        btnToolBarButtonBack.setOnClickListener {
            listener.onBackToStandard()
        }
    }

    private fun initView(listSongName: String?) {
        if (listSongName != null) {
            tvName.text = listSongName
        } else {

        }
    }

    private fun initRecyclerView() {
        recyclerViewSong.layoutManager = LinearLayoutManager(context)
        songAdapter = SongAdapter(songs, context, object : SongViewHolderListener {
            override fun onFavoriteChange(position: Int) {
                val targetSong = songs[position]
                targetSong.changeFavouriteState()
                songAdapter.notifyDataSetChanged()
            }

            override fun onStartListen(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        recyclerViewSong.adapter = songAdapter
    }

    private fun getFragmentName(bundle: Bundle?): String? {
        val type = bundle?.getInt(KEY_TYPE, 0)
        return when (type) {
            TYPE_HISTORY -> resources.getString(R.string.history)
            TYPE_FAVORITE -> resources.getString(R.string.favourite)
            else -> null
        }
    }

    private fun initSongs(playlistName: String?) {
        if (playlistName != null) {
            initSongFromPlayList(playlistName)
        } else {
            initAllSong()
        }
    }

    private fun initSongFromPlayList(playlistName: String) {
        songs.clear()
        context?.let {
            AppDataHelper.getInstance(it).getSongInPlaylist(playlistName, object : SongUpdater {
                override fun getSongResult(result: List<Song>) {
                    songs.addAll(result)
                }
            })
        }
    }

    private fun initAllSong() {
        songs.clear()
        songs.addAll((activity as MainActivity).songs)
    }

    companion object {
        private const val KEY_TYPE = "KEY_TYPE"
        const val TYPE_ALL = 0
        const val TYPE_HISTORY = 1
        const val TYPE_FAVORITE = 2

        fun instance(@ListSongFragmentType type: Int): ListSongFragment {
            val timelineFragment = ListSongFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_TYPE, type)
            timelineFragment.arguments = bundle
            return timelineFragment
        }
    }

    @IntDef(TYPE_HISTORY, TYPE_FAVORITE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ListSongFragmentType
}
