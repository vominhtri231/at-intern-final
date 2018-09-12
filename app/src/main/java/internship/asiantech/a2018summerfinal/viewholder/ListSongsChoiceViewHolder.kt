package internship.asiantech.a2018summerfinal.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R

class ListSongsChoiceViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val tvNameSongListChoice : TextView? = itemView?.findViewById(R.id.tvItemNameSongListChoice)
    val tvAristListChoice : TextView? = itemView?.findViewById(R.id.tvSongArtistListChoice)
    val rbSonglistChoice: RadioButton? = itemView?.findViewById(R.id.rbSonglistChoice)
}