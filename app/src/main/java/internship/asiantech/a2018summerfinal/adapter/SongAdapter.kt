package internship.asiantech.a2018summerfinal.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.MusicAdapter.MusicHolder
import internship.asiantech.a2018summerfinal.database.model.Song

class MusicAdapter(private val musics: List<Song>, private val context: Context,
                   private val onFavouriteListener: (position: Int) -> Unit)
    : RecyclerView.Adapter<MusicHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_music, parent, false)
        return MusicHolder(view)
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
        holder.tvTitleSong?.text = musics[position].title
        holder.tvSinger?.text = musics[position].artist
        if (musics[position].isFavourite) {
            holder.imgFavourite?.setImageResource(R.drawable.ic_like)
        } else {
            holder.imgFavourite?.setImageResource(R.drawable.ic_unlike)
        }
    }

    inner class MusicHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvTitleSong = itemView?.findViewById<TextView>(R.id.tvTitleSong)
        val tvSinger = itemView?.findViewById<TextView>(R.id.tvSinger)
        val imgFavourite = itemView?.findViewById<ImageView>(R.id.imgFavourite)

        init {
            imgFavourite?.setOnClickListener {
                val position = layoutPosition
                onFavouriteListener(position)
            }

            itemView?.setOnClickListener {
                val position = layoutPosition
            }
        }
    }
}
