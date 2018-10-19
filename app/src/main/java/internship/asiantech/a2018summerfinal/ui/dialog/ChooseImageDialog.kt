package internship.asiantech.a2018summerfinal.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ArrayAdapter
import internship.asiantech.a2018summerfinal.ui.dialog.listener.ChooseImageEventListener

class ChooseImageDialog : DialogFragment() {
    private lateinit var listener: ChooseImageEventListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf("Camera", "Gallery")
        val adapter = ArrayAdapter(activity, android.R.layout.select_dialog_item, items)
        val builder = AlertDialog.Builder(activity as Activity)
        builder.setTitle("Add Photo")
        builder.setAdapter(adapter) { _, which ->
            Log.e(TAG, which.toString())
            when (which) {
                0 -> listener.open(ChosenImageType.CameraType)
                1 -> listener.open(ChosenImageType.GalleryType)
            }
        }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ChooseImageEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ChooseImageEventListener"))
        }
    }

    enum class ChosenImageType {
        CameraType,
        GalleryType
    }

    companion object {
        private val TAG = ChooseImageDialog::class.qualifiedName
        const val NAME = "choose image"
    }
}
