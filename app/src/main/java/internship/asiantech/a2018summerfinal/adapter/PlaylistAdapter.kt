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
import internship.asiantech.a2018summerfinal.fragment.ButtonEventListener
import internship.asiantech.a2018summerfinal.viewholder.PlaylistViewHolder

class PlaylistAdapter(var listPlaylist: MutableList<Playlist>, val context: Context, val getPosition: (Int) -> Unit) : RecyclerView.Adapter<PlaylistViewHolder>(), ButtonEventListener {


    private var mIsShowButtonClose: Boolean = false
    private var listSongUpdater = mutableListOf<Song>()
    private lateinit var mListenerButtonDelete: ButtonEventListener
    private lateinit var mUpdater: SongUpdater

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
//        holder.tvCountSongOfPlaylist?.text = String.format("%s", playlist.listSong.size, context.resources.getString(R.string.song))
        if (mIsShowButtonClose) {
            holder.imgShowDeletePlaylist?.visibility = View.GONE
        } else {
            holder.imgShowDeletePlaylist?.visibility = View.VISIBLE
        }

        /*handle item on viewholder*/
        val name = (listPlaylist.get(position).name)
        mUpdater.getSongResult(listSongUpdater)

        holder.llOpenPlaylist?.setOnClickListener(View.OnClickListener {
            AppDataHelper.getInstance(context).getSongInPlaylist(name, mUpdater)
            getPosition(position)
        })

        holder.imgShowDeletePlaylist?.setOnClickListener(View.OnClickListener {
            AppDataHelper.getInstance(context).deletePlaylist(name)
        })
    }

    fun setStateShowButton(isClosed: Boolean) {
        mIsShowButtonClose = isClosed
        notifyDataSetChanged()
    }

    override fun onDeletePlaylist() {
        notifyDataSetChanged()
    }

    private fun setListenerItemPlaylist(holder: PlaylistViewHolder, position: Int) {

        val name = (listPlaylist.get(position).name)
        mUpdater.getSongResult(listSongUpdater)

        holder.llOpenPlaylist?.setOnClickListener(View.OnClickListener {
            AppDataHelper.getInstance(context).getSongInPlaylist(name, mUpdater)
        })

        holder.imgShowDeletePlaylist?.setOnClickListener(View.OnClickListener {
            AppDataHelper.getInstance(context).deletePlaylist(name)
        })
    }

//    fun addListPlaylist(list: MutableList<Playlist>){
//        listPlaylist = list
//    notifyDataSetChanged()
//    }
}
