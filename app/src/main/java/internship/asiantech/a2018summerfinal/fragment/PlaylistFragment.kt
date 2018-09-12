package internship.asiantech.a2018summerfinal.fragment

import android.content.Context
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
import internship.asiantech.a2018summerfinal.model.Playlist

class PlaylistFragment : Fragment() {
    private lateinit var mImgCreatePlaylist : ImageButton
    private lateinit var mImgChangePlaylist: ImageButton
    private lateinit var listPlaylist: MutableList<Playlist>
    private lateinit var mContext :Context
    private var mAdapterPlaylist = PlaylistAdapter(listPlaylist,mContext)
    private lateinit var mRecyclerviewPlaylist: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        mImgCreatePlaylist = view.findViewById(R.id.imgAddPlaylist)
        mImgChangePlaylist = view.findViewById(R.id.imgChangePlaylist)
        mRecyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist)
        mLinearLayoutManager = LinearLayoutManager(mContext)
        mRecyclerviewPlaylist.layoutManager = mLinearLayoutManager
        mRecyclerviewPlaylist.setHasFixedSize(true)
        mAdapterPlaylist = PlaylistAdapter(listPlaylist,mContext)
        mRecyclerviewPlaylist.adapter = mAdapterPlaylist
    }
}
