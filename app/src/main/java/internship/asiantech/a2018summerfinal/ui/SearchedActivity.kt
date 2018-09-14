package internship.asiantech.a2018summerfinal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.MusicAdapter
import internship.asiantech.a2018summerfinal.librarysong.ListMusicActivity
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import internship.asiantech.a2018summerfinal.model.Music
import kotlinx.android.synthetic.main.fragment_list_songs.*

class SearchedActivity : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private var musics: MutableList<Music> = ArrayList()
    private var keySearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched)
        keySearch = intent.getStringExtra(ListMusicActivity.KEY_SEARCH)
        val listMusic = ListMusic(this)
        musics=getListSearch(listMusic.getListMusics(), keySearch) as MutableList<Music>
        initRecyclerView()
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
