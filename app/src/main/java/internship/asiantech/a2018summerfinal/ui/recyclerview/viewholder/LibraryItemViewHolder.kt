package internship.asiantech.a2018summerfinal.ui.recyclerview.viewholder

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.recyclerview.LibraryItem
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LibraryFragmentActionListener

class LibraryItemViewHolder(view: View?, listener: LibraryFragmentActionListener) : RecyclerView.ViewHolder(view) {
    private val tvType = view?.findViewById<TextView>(R.id.tvType)

    init {
        itemView.setOnClickListener {
            when (tvType?.text.toString()) {
                LibraryItem.HISTORY_TYPE -> listener.openHistorySong()
                LibraryItem.FAVORITE_TYPE -> listener.openFavoriteSong()
                LibraryItem.ALL_SONG_TYPE -> listener.openAllSong()
            }
        }
    }

    fun setIcon(drawable: Drawable?) {
        tvType?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    fun setName(name: String) {
        tvType?.text = name
    }
}
