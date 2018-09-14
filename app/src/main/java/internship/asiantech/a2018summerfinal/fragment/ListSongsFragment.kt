package internship.asiantech.a2018summerfinal.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music

class ListSongsFragment : Fragment() {
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
}
