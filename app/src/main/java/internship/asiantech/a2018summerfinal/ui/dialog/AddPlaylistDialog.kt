package internship.asiantech.a2018summerfinal.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.EditText
import internship.asiantech.a2018summerfinal.R

class AddPlaylistDialog : DialogFragment() {
    lateinit var listener: AddPlaylistEventListener
    lateinit var edtNamePlaylist: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddPlaylistEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement AddPlaylistEventListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val rootView: View = View.inflate(activity, R.layout.dialog_add_playlist, null)
        edtNamePlaylist = rootView.findViewById(R.id.edtNamePlaylist)
        builder.setView(rootView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    listener.addPlaylist(edtNamePlaylist.text.toString())
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
        return builder.create()
    }

    companion object {
        const val NAME = "Add playlist"
    }
}
