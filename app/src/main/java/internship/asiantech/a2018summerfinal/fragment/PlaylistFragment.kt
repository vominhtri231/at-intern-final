package internship.asiantech.a2018summerfinal.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.PlaylistAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.dialog.DialogCreatePlaylist
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import internship.asiantech.a2018summerfinal.ChoiceSongActivity
import internship.asiantech.a2018summerfinal.ChoiceSongActivity.Companion.RESULT_KEY
import internship.asiantech.a2018summerfinal.database.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.fragment.dialog.EventAddPlaylistListener
import internship.asiantech.a2018summerfinal.ui.ListSongInPlaylistActivity
import kotlinx.android.synthetic.main.fragment_playlist.*

@Suppress("DEPRECATION")
class PlaylistFragment : Fragment(), EventAddPlaylistListener, EventClickItemOpenListSong {
    private lateinit var mTvCreatePlaylist: TextView
    private lateinit var mTvChangePlaylist: TextView
    private var listPlaylist: MutableList<Playlist> = mutableListOf()
    private lateinit var mAdapterPlaylist: PlaylistAdapter
    private lateinit var mRecyclerviewPlaylist: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var mIsShowCloseButton = false
    var namePlayList = ""

    companion object {
        const val ADD_PLAYLIST = 200
        const val KEY_POSITION = "key_position"
        const val KEY_ID_PLAYLIST_BUNDLE = "keyIdNamePlaylist"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        initViews(view)
        setListener()
        return view
    }

    private fun getPlayList() {
        context?.let {
            AppDataHelper.getInstance(it).getAllPlaylist(object : PlaylistUpdater {
                override fun getPlaylistResult(result: List<Playlist>) {
                    listPlaylist.clear()
                    listPlaylist.addAll(result)
                    mAdapterPlaylist.notifyDataSetChanged()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        getPlayList()
    }

    private fun initViews(view: View) {
        mTvCreatePlaylist = view.findViewById(R.id.tvAddPlaylist)
        mTvChangePlaylist = view.findViewById(R.id.tvChangePlaylist)
        mRecyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mRecyclerviewPlaylist.layoutManager = mLinearLayoutManager
        //get list playlist
        context?.let {
            mAdapterPlaylist = PlaylistAdapter(listPlaylist, it, this) { it ->
                val intent = Intent(context, ChoiceSongActivity::class.java)
                intent.putExtra(KEY_POSITION, it)
                activity?.startActivity(intent)
            }
        }
        mRecyclerviewPlaylist.setHasFixedSize(true)
        mRecyclerviewPlaylist.adapter = mAdapterPlaylist
    }

    private fun setListener() {
        mTvCreatePlaylist.setOnClickListener {
            val fm = DialogCreatePlaylist()
            fm.mListener = this
            fm.isCancelable = true
            val activity = activity as AppCompatActivity
            fm.show(activity.supportFragmentManager, "create playlist")

        }
        mTvChangePlaylist.setOnClickListener {
            mIsShowCloseButton = !mIsShowCloseButton
            mAdapterPlaylist.setStateShowButton(mIsShowCloseButton)
        }
    }

    override fun addObjectOnClick(position: Int) {
        val namePlaylist = listPlaylist[position].name
        val intent = Intent(context, ListSongInPlaylistActivity::class.java)
        intent.putExtra(KEY_ID_PLAYLIST_BUNDLE, namePlaylist)
        startActivity(intent)
    }

    override fun addPlaylist(name: String, listSongs: MutableList<Song>) {
        context?.let {
            AppDataHelper.getInstance(it).addPlaylist(Playlist(name))
            namePlayList = name
            val intent = Intent(activity, ChoiceSongActivity::class.java)
            intent.putExtra(KEY_ID_PLAYLIST_BUNDLE, namePlayList)
            startActivityForResult(intent, ADD_PLAYLIST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLAYLIST && resultCode == RESULT_OK) {
            val listSongs = data?.getParcelableArrayListExtra<Song>(RESULT_KEY)!!
                mAdapterPlaylist.notifyDataSetChanged()

        }
    }
}
