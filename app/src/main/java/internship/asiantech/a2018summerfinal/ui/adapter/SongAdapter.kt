package internship.asiantech.a2018summerfinal.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.ui.viewholder.SongViewHolder
import internship.asiantech.a2018summerfinal.ui.viewholder.SongViewHolderListener

class SongAdapter(private val musics: List<Song>, private val context: Context?,
                  private val listener: SongViewHolderListener)
    : RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_music, parent, false)
        return SongViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.tvTitleSong?.text = musics[position].title
        holder.tvSinger?.text = musics[position].artist
        holder.index = position
        if (musics[position].isFavourite) {
            holder.imgFavourite?.setImageResource(R.drawable.ic_like)
        } else {
            holder.imgFavourite?.setImageResource(R.drawable.ic_unlike)
        }
    }
}
