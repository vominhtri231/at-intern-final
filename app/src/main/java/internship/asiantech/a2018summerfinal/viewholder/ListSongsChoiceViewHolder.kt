package internship.asiantech.a2018summerfinal.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R

class ListSongsChoiceViewHolder(itemView: View, listener : RadioButtonEventChoice  ) : RecyclerView.ViewHolder(itemView) {

    val tvNameSongListChoice: TextView = itemView.findViewById(R.id.tvItemNameSongListChoice)
    val tvAristListChoice: TextView = itemView.findViewById(R.id.tvSongArtistListChoice)
    val rbSonglistChoice: RadioButton = itemView.findViewById(R.id.rbSonglistChoice)
    val llPlaylistChoice: LinearLayout = itemView.findViewById(R.id.llPlaylistChoice)


    init {
        llPlaylistChoice.setOnClickListener(View.OnClickListener {
            val isCheck = rbSonglistChoice.isChecked.not()
            rbSonglistChoice.isChecked = isCheck
            listener.onRadioButtonClickListener(adapterPosition,isCheck)
        })
    }

}