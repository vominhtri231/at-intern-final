package internship.asiantech.a2018summerfinal.fragment.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song

class DialogCreatePlaylist() : DialogFragment() {
    private lateinit var mEdtInputNamePlaylist : EditText
    private lateinit var mListener : AddPlaylistDialogListener
    private val mDataListPlaylists = mutableListOf<Song>()

    val ADD_PLAYLIST_DIALOG_TAG = "add playlist tag"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lateinit var rootView : View
        rootView = inflater.inflate(R.layout.dialog_create_new_playlist,container,false)
        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         var builder = AlertDialog.Builder(activity)
         var rootView : View = View.inflate(activity,R.layout.dialog_create_new_playlist, null)
        mEdtInputNamePlaylist = rootView.findViewById(R.id.edtNamePlaylist)
        builder.setView(rootView)
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialogInterface, i ->
                    mListener.addPlaylist(mEdtInputNamePlaylist.text.toString(),mDataListPlaylists)
                })
                .setNegativeButton(R.string.exit, DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.cancel()
                })

        return builder.create()

    }

    override fun onAttach(activity: Activity) {
        if (activity is AddPlaylistDialogListener) {
            mListener = activity as AddPlaylistDialogListener
        }

        super.onAttach(activity)
    }
//    private fun createNewPlaylist(name: String, listSongs : MutableList<Song>){
//        val playlist = Playlist(name, listSongs)
//        dialog.show()
//
//
//    }
    interface AddPlaylistDialogListener{
        fun addPlaylist(name : String,listSongs : MutableList<Song>)
    }
}