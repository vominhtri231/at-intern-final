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
import internship.asiantech.a2018summerfinal.ui.adapter.SongAdapter
import internship.asiantech.a2018summerfinal.ui.viewholder.SongViewHolderListener
import internship.asiantech.a2018summerfinal.utils.hideKeyboard
import internship.asiantech.a2018summerfinal.utils.searchSong
import kotlinx.android.synthetic.main.fragment_search_song.*

class SearchSongFragment : Fragment() {
    private lateinit var songAdapter: SongAdapter
    private var searchedSongs: MutableList<Song> = ArrayList()

    private lateinit var listener: BackEventListener
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
        if (context is BackEventListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement BackEventListener")
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
        songAdapter = SongAdapter(searchedSongs, context, object : SongViewHolderListener {
            override fun onFavoriteChange(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStartListen(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        recyclerViewSong.adapter = songAdapter
    }

    private fun search(keySearch: String) {
        searchedSongs.clear()
        searchedSongs.addAll(searchSong((activity as MainActivity).songs, keySearch))
    }
}
