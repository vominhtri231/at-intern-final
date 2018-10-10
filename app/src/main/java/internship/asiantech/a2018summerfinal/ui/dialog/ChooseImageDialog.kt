package internship.asiantech.a2018summerfinal.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ArrayAdapter

class ChooseImageDialog : DialogFragment() {
    companion object {
        private val TAG = ChooseImageDialog::class.qualifiedName
    }

    private lateinit var listener: ChooseImageListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf("Camera", "Gallery")
        val adapter = ArrayAdapter(activity, android.R.layout.select_dialog_item, items)
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add Photo")
            builder.setAdapter(adapter) { _, which ->
                Log.e(TAG, which.toString())
                when (which) {
                    0 -> listener.open(ChosenImageType.CameraType)
                    1 -> listener.open(ChosenImageType.GalleryType)
                }
            }
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ChooseImageListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ChooseImageListener"))
        }
    }

    interface ChooseImageListener {
        fun open(type: ChosenImageType)
    }

    enum class ChosenImageType {
        CameraType,
        GalleryType
    }
}