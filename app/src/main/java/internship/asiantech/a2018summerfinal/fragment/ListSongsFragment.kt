package internship.asiantech.a2018summerfinal.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music
import internship.asiantech.a2018summerfinal.adapter.MusicAdapter
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import kotlinx.android.synthetic.main.fragment_list_songs.*

class ListSongsFragment : Fragment() {
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var musics: MutableList<Music>

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

    private fun getListSearch(listMusics: List<Music>, strSearch: String): List<Music> {
        val listSearch = mutableListOf<Music>()
        for (music in listMusics) {
            if (isWordInString(strSearch, music.songTitle)) {
                listSearch.add(music)
                continue
            }
            if (isWordInString(strSearch, music.artist)) {
                listSearch.add(music)
                continue
            }
        }
        return listSearch
    }

    private fun isWordInString(strSearch: String, str: String): Boolean {
        val words = strSearch.split(" ".toRegex())
        for (word in words) {
            return str.contains(strSearch)
        }
        return false
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
                musicAdapter.notifyDataSetChanged()
            }
            recyclerViewMusic.adapter = musicAdapter
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
                getAllist()
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
    }

    private fun getFavouriteList() {
        musics = ArrayList()
    }

    private fun getAllist() {
        context?.let {
            musics = ArrayList()
            val listMusic = ListMusic(it)
            musics = listMusic.getListMusics()
        }
    }
}
