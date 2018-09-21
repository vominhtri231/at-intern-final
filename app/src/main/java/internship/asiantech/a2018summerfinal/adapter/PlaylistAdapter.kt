package internship.asiantech.a2018summerfinal.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.SongUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.EventClickItemOpenListSong
import internship.asiantech.a2018summerfinal.viewholder.PlaylistViewHolder

class PlaylistAdapter(var listPlaylist: MutableList<Playlist>, val context: Context, private val listener: EventClickItemOpenListSong, val getPosition: (Int) -> Unit) : RecyclerView.Adapter<PlaylistViewHolder>() {
    private var mIsShowButtonClose: Boolean = false
    private var listSongUpdater = mutableListOf<Song>()

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
        /*holder.tvCountSongOfPlaylist?.text = String.format("%s%s", listSongUpdater.size.toString(), context.resources.getString(R.string.song))*/
        if (mIsShowButtonClose) {
            holder.imgShowDeletePlaylist?.visibility = View.VISIBLE
        } else {
            holder.imgShowDeletePlaylist?.visibility = View.GONE
        }

        holder.llOpenPlaylist?.setOnClickListener {
            listener.addObjectOnClick(position)
        }
        holder.imgShowDeletePlaylist?.setOnClickListener {
            deletePlaylist(playlist)
        }
    }

    fun setStateShowButton(isClosed: Boolean) {
        mIsShowButtonClose = isClosed
        notifyDataSetChanged()
    }
    private fun deletePlaylist(playlist: Playlist) {
        AppDataHelper.getInstance(context).deletePlaylist(playlist.name)
        listPlaylist.remove(playlist)
        notifyDataSetChanged()
    }
}
