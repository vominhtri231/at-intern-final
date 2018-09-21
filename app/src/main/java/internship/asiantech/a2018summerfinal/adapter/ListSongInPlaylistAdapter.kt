package internship.asiantech.a2018summerfinal.adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.EventClickItemSongListener

class ListSongInPlaylistAdapter(private val listSongInPlaylist: MutableList<Song>,
                                private val context: Context,
                                private val onDeleteSonglistener: EventClickItemSongListener)
    : RecyclerView.Adapter<ListSongInPlaylistAdapter.ListSongInPlayListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSongInPlayListViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_music, parent, false)
        return ListSongInPlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSongInPlaylist.size
    }

    override fun onBindViewHolder(holder: ListSongInPlayListViewHolder, position: Int) {
        holder.tvTitleSong?.text = listSongInPlaylist[position].name
        holder.tvSinger?.text = listSongInPlaylist[position].author
    }

    inner class ListSongInPlayListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvTitleSong = itemView?.findViewById<TextView>(R.id.tvTitleSong)
        val tvSinger = itemView?.findViewById<TextView>(R.id.tvSinger)
        val rlItemSongInPlaylist = itemView?.findViewById<RelativeLayout>(R.id.rlItemSongInPlaylist)
        init {
            rlItemSongInPlaylist?.setOnClickListener {
                val position = layoutPosition
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.delete_song)
                        .setMessage(R.string.message_delete_song)
                        .setNegativeButton(R.string.no) { dialogInterface, _ ->
                            dialogInterface.cancel()
                        }
                        .setPositiveButton(R.string.ok) { _, _ ->
                            onDeleteSonglistener.deleteSongOnClick(position)
                        }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
}
