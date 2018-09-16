package internship.asiantech.a2018summerfinal.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.PlaylistAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.dialog.DialogCreatePlaylist
import android.support.v7.app.AppCompatActivity
import internship.asiantech.a2018summerfinal.ChoiceSongActivity
import internship.asiantech.a2018summerfinal.ChoiceSongActivity.Companion.RESULT_KEY
import internship.asiantech.a2018summerfinal.database.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.fragment.dialog.EventAddPlaylistListener


class PlaylistFragment : Fragment(), EventAddPlaylistListener {

    private lateinit var mImgCreatePlaylist: ImageButton
    private lateinit var mImgChangePlaylist: ImageButton
    private var listPlaylist: MutableList<Playlist> = mutableListOf()
    private lateinit var mContext: Context
    lateinit var updater: PlaylistUpdater

    val KEY_POSITION = "key_position"
    private lateinit var mAdapterPlaylist: PlaylistAdapter
    private lateinit var mRecyclerviewPlaylist: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var mIsShowCloseButton = false
    var namePlayList =""
    companion object {
        const  val ADD_PLAYLIST = 200
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setListener()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

            mContext = context!!


        context?.let {
            AppDataHelper.getInstance(it).getAllPlaylist(object : PlaylistUpdater {
                override fun getPlaylistResult(result: List<Playlist>) {
                     listPlaylist = result.toMutableList()
                }

            })
        }
    }

    private fun initViews(view: View) {

        mImgCreatePlaylist = view.findViewById(R.id.imgAddPlaylist)
        mImgChangePlaylist = view.findViewById(R.id.imgChangePlaylist)
        mRecyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mRecyclerviewPlaylist.layoutManager = mLinearLayoutManager
        mRecyclerviewPlaylist.setHasFixedSize(true)

        //get list playlist
        mAdapterPlaylist = PlaylistAdapter(listPlaylist, mContext) { it ->
            var intent = Intent(mContext, ChoiceSongActivity::class.java)
            intent.putExtra(KEY_POSITION, it)
            activity?.startActivity(intent)
        }
        mRecyclerviewPlaylist.adapter = mAdapterPlaylist
    }

    private fun setListener() {
        mImgCreatePlaylist.setOnClickListener(View.OnClickListener {
            mIsShowCloseButton = false
//           mAdapterPlaylist.setStateShowButton(mIsShowCloseButton)
            val fm = DialogCreatePlaylist()
            fm.mListener = this
            fm.setCancelable(true)
            val activity = activity as AppCompatActivity
            fm.show(activity.supportFragmentManager, "create playlist")

        })
        mImgChangePlaylist.setOnClickListener(View.OnClickListener {
            mIsShowCloseButton = !mIsShowCloseButton
            mAdapterPlaylist.setStateShowButton(mIsShowCloseButton)
        })
    }
//
//    private fun createDialog() {
//        val dialog = DialogCreatePlaylist()
//        dialog.show(activity?.supportFragmentManager, dialog.ADD_PLAYLIST_DIALOG_TAG)
//    }

    override fun addPlaylist(name: String, listSongs: MutableList<Song>) {
        AppDataHelper.getInstance(mContext).addPlaylist(Playlist(name))
        namePlayList = name
        for (song in listSongs) {
            AppDataHelper.getInstance(mContext).addSong(song = song)
            AppDataHelper.getInstance(mContext).addSongToPlaylist(name, song.id)
            mAdapterPlaylist.notifyDataSetChanged()
        }
        val intent = Intent(activity, ChoiceSongActivity::class.java)
        startActivityForResult(intent,ADD_PLAYLIST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLAYLIST && resultCode == RESULT_OK){
            val  listSongs = data?.getParcelableArrayListExtra<Song>(RESULT_KEY)!!
            for (song in listSongs) {
                AppDataHelper.getInstance(mContext).addSong(song = song)
                AppDataHelper.getInstance(mContext).addSongToPlaylist(namePlayList, song.id)
                mAdapterPlaylist.notifyDataSetChanged()
            }

        }
    }
//    private fun getPlaylist(){
//        lateinit var updater: PlaylistUpdater
//        updater.getPlaylistResult(listPlaylists)
//        AppDataHelper.getInstance(mContext).getAllPlaylist(updater = this)
//    }

}
