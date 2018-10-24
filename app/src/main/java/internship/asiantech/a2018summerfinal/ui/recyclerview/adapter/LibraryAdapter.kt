package internship.asiantech.a2018summerfinal.ui.recyclerview.adapter

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LibraryFragmentActionListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.LibraryItem
import internship.asiantech.a2018summerfinal.ui.recyclerview.LibraryItem.Companion.ALL_SONG_TYPE
import internship.asiantech.a2018summerfinal.ui.recyclerview.LibraryItem.Companion.FAVORITE_TYPE
import internship.asiantech.a2018summerfinal.ui.recyclerview.LibraryItem.Companion.HISTORY_TYPE
import internship.asiantech.a2018summerfinal.ui.recyclerview.viewholder.LibraryItemViewHolder

class LibraryAdapter(val context: Context?,
                     val listener: LibraryFragmentActionListener)
    : RecyclerView.Adapter<LibraryItemViewHolder>() {
    private val libraryItems: MutableList<LibraryItem> = mutableListOf()

    init {
        libraryItems.add(LibraryItem(ALL_SONG_TYPE, R.drawable.ic_illuminati))
        libraryItems.add(LibraryItem(HISTORY_TYPE, R.drawable.ic_history))
        libraryItems.add(LibraryItem(FAVORITE_TYPE, R.drawable.ic_heart))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryItemViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.viewholder_library_item, parent, false)
        return LibraryItemViewHolder(view, listener)
    }

    override fun getItemCount(): Int = libraryItems.size

    override fun onBindViewHolder(holder: LibraryItemViewHolder, position: Int) {
        val libraryItem = libraryItems[position]
        context?.let {
            val icon = ResourcesCompat.getDrawable(it.resources,
                    libraryItem.imageId, null)
            holder.setIcon(icon)
        }
        holder.setName(libraryItem.name)
    }
}
