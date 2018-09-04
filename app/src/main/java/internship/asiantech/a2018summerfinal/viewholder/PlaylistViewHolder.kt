package internship.asiantech.a2018summerfinal.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R
import kotlinx.android.synthetic.main.fragment_playlist.view.*

class PlaylistViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val tvNamePlaylist : TextView? = itemView?.findViewById(R.id.tvNamePlaylist)
    val tvCountSongOfPlaylist : TextView? = itemView?.findViewById(R.id.tvCountSongOfPlaylist)
}
