package internship.asiantech.a2018summerfinal.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song

class DialogCreatePlaylist() : DialogFragment() {
    private lateinit var mEdtInputNamePlaylist: EditText
    lateinit var mListener: EventAddPlaylistListener
    private val mDataListPlaylists = mutableListOf<Song>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View
        rootView = inflater.inflate(R.layout.dialog_create_new_playlist, container, false)
        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val rootView: View = View.inflate(activity, R.layout.dialog_create_new_playlist, null)
        mEdtInputNamePlaylist = rootView.findViewById(R.id.edtNamePlaylist)
        builder.setView(rootView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    mListener.addPlaylist(mEdtInputNamePlaylist.text.toString(), mDataListPlaylists)
                }
                .setNegativeButton(R.string.exit) { dialogInterface, i ->

                    dialogInterface.cancel()
                }
        return builder.create()
    }
}
