package internship.asiantech.a2018summerfinal.ui.fragment


import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.fragment.listener.ListSongFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.SongAdapter
import internship.asiantech.a2018summerfinal.utils.hideKeyboard
import internship.asiantech.a2018summerfinal.utils.searchSong
import kotlinx.android.synthetic.main.fragment_addition.*
import kotlinx.android.synthetic.main.addition_search_song_header.*
import kotlinx.android.synthetic.main.addition_recylerview.*

class SearchSongFragment : AdditionFragment() {
    private lateinit var listSongFragmentActionListener: ListSongFragmentActionListener
    private var searchedSongs: MutableList<Song> = ArrayList()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ListSongFragmentActionListener) {
            listSongFragmentActionListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ListSongFragmentActionListener")
        }
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter =  SongAdapter(searchedSongs, context, listSongFragmentActionListener)
        edtSearchInput.setOnKeyListener(View.OnKeyListener { _, key, keyEvent ->
            if (keyEvent?.action == KeyEvent.ACTION_DOWN
                    && key == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard(activity as Activity)
                search(edtSearchInput.text.toString())
                return@OnKeyListener true
            }
            false
        })
        edtSearchInput.requestFocus()
    }

    override fun addDiffViews(inflater: LayoutInflater) {
        inflater.inflate(R.layout.addition_search_song_header, flDiff)
        inflater.inflate(R.layout.addition_recylerview, flAdditionContent)
    }

    private fun search(keySearch: String) {
        searchedSongs.clear()
        searchedSongs.addAll(searchSong((activity as MainActivity).getSongs(), keySearch))
        recyclerView.adapter.notifyDataSetChanged()
    }
}
