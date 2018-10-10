package internship.asiantech.a2018summerfinal.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.MusicAdapter
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import kotlinx.android.synthetic.main.fragment_list_songs.*

class SearchedActivity : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private var musics: MutableList<Song> = ArrayList()
    private var keySearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched)
        keySearch = intent.getStringExtra(ListMusicActivity.KEY_SEARCH)
        val listMusic = ListMusic(this)
        musics = getListSearch(listMusic.getListMusics(), keySearch) as MutableList<Song>
        initRecyclerView()
    }

    private fun getListSearch(songs: List<Song>, strSearch: String): List<Song> {
        val listSearch = mutableListOf<Song>()
        for (song in songs) {
            if (isWordInString(strSearch, song.title)) {
                listSearch.add(song)
                continue
            }
            if (isWordInString(strSearch, song.artist)) {
                listSearch.add(song)
                continue
            }
        }
        return listSearch
    }

    private fun isWordInString(strSearch: String, str: String): Boolean {
        val words = strSearch.split(" ".toRegex())
        for (word in words) {
            return str.toLowerCase().contains(word.toLowerCase())
        }
        return false
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerViewMusic.layoutManager = layoutManager
        musicAdapter = MusicAdapter(musics, this) { position ->
            musics[position].isFavourite = !musics[position].isFavourite
            musicAdapter.notifyDataSetChanged()
        }
        recyclerViewMusic.adapter = musicAdapter
    }
}
