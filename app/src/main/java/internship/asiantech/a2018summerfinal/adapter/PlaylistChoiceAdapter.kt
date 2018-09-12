package internship.asiantech.a2018summerfinal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music
import internship.asiantech.a2018summerfinal.viewholder.ListSongsChoiceViewHolder

class PlaylistChoiceAdapter(var listSongsPlaylist: List<Music>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_choice_song_add_playlist,parent,false)
        return ListSongsChoiceViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listSongsPlaylist.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    }
}