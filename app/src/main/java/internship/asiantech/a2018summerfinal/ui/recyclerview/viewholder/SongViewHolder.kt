package internship.asiantech.a2018summerfinal.ui.recyclerview.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.recyclerview.listener.SongViewHolderListener

class SongViewHolder(itemView: View?,
                     private val listener: SongViewHolderListener)
    : RecyclerView.ViewHolder(itemView) {
    var index = 0
    val tvTitleSong = itemView?.findViewById<TextView>(R.id.tvTitleSong)
    val tvSinger = itemView?.findViewById<TextView>(R.id.tvSinger)
    val imgFavourite = itemView?.findViewById<ImageView>(R.id.imgFavourite)

    init {
        imgFavourite?.setOnClickListener {
            listener.onFavoriteChange(index)
        }

        itemView?.setOnClickListener {
            listener.onStartListen(index)
        }
    }
}
