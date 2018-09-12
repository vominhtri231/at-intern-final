package internship.asiantech.a2018summerfinal.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.Music
import internship.asiantech.a2018summerfinal.model.Playlist
import internship.asiantech.a2018summerfinal.viewholder.PlaylistViewHolder

class PlaylistAdapter(var listPlaylist: MutableList<Playlist>, val context: Context) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var mIsShowButtonClose: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPlaylist.size

    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = listPlaylist[position]
        holder.tvNamePlaylist?.text = playlist.name
        holder.tvCountSongOfPlaylist?.text = String.format("%s", playlist.listSong.size, context.resources.getString(R.string.song))
        if (mIsShowButtonClose) {
            holder.imgShowDeletePlaylist?.visibility = View.GONE
        } else {
            holder.imgShowDeletePlaylist?.visibility = View.VISIBLE
        }

    }

    fun setStateShowButton(isClosed: Boolean) {
        mIsShowButtonClose = isClosed
        notifyDataSetChanged()
    }
}
