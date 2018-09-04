package internship.asiantech.a2018summerfinal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music
import internship.asiantech.a2018summerfinal.viewholder.PlaylistViewHolder

class PlaylistAdapter( listPlaylist : MutableList<Music>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mlistPlayList : MutableList<Music> = listPlaylist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.fragment_playlist, parent,false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlistPlayList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder as PlaylistViewHolder,position)


    }
    private fun onBindViewHolder(playlistViewHolder: PlaylistViewHolder,position: Int){

    }
}
