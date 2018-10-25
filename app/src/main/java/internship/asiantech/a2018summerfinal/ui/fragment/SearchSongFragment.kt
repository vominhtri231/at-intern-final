package internship.asiantech.a2018summerfinal.ui.fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.fragment.listener.AdditionFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.fragment.listener.ListSongFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.SongAdapter
import internship.asiantech.a2018summerfinal.utils.hideKeyboard
import internship.asiantech.a2018summerfinal.utils.searchSong
import kotlinx.android.synthetic.main.fragment_search_song.*

class SearchSongFragment : Fragment() {
    private lateinit var songAdapter: SongAdapter
    private lateinit var listener: AdditionFragmentActionListener
    private lateinit var listSongFragmentActionListener: ListSongFragmentActionListener
    private var searchedSongs: MutableList<Song> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initRecyclerView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AdditionFragmentActionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ListSongFragmentActionListener")
        }

        if (context is ListSongFragmentActionListener) {
            listSongFragmentActionListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ListSongFragmentActionListener")
        }
    }

    private fun setListeners() {
        btnToolBarButtonBack.setOnClickListener {
            listener.onBackToStandard()
        }
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

    private fun initRecyclerView() {
        recyclerViewSong.layoutManager = LinearLayoutManager(context)
        songAdapter = SongAdapter(searchedSongs, context, listSongFragmentActionListener)
        recyclerViewSong.adapter = songAdapter
    }

    private fun search(keySearch: String) {
        searchedSongs.clear()
        searchedSongs.addAll(searchSong((activity as MainActivity).getSongs(), keySearch))
    }
}
