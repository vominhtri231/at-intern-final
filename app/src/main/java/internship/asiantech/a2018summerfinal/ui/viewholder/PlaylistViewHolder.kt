package internship.asiantech.a2018summerfinal.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R

class PlaylistViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val tvNamePlaylist: TextView? = itemView?.findViewById(R.id.tvNamePlaylist)
    val tvCountSongOfPlaylist: TextView? = itemView?.findViewById(R.id.tvCountSongOfPlaylist)
    val imgShowDeletePlaylist : ImageView? = itemView?.findViewById(R.id.btnCloseShowDelete)
    val llOpenPlaylist : LinearLayout? = itemView?.findViewById(R.id.llOpenPlaylist)
}
