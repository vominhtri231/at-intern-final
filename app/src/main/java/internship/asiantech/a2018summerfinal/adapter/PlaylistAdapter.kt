package internship.asiantech.a2018summerfinal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music
import internship.asiantech.a2018summerfinal.viewholder.PlaylistViewHolder

class PlaylistAdapter(var listPlaylist : MutableList<Music>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_playlist_item, parent,false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPlaylist.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder as PlaylistViewHolder,position)
    }
}
