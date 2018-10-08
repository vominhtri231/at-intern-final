package internship.asiantech.a2018summerfinal.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter

class ChooseImageDialog : DialogFragment() {
    private lateinit var listener: ChooseImageListerner

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val items = arrayOf("Camera", "Gallery")
        val adapter = ArrayAdapter(activity, android.R.layout.select_dialog_item, items)
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add Photo")
            builder.setAdapter(adapter) { _, which ->
                when (which) {
                    1 -> listener.open(ChoosedImageType.CameraType)
                    2 -> listener.open(ChoosedImageType.GalleryType)
                }
            }
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ChooseImageListerner
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ChooseImageListerner"))
        }
    }

    interface ChooseImageListerner {
        fun open(choosedype: ChoosedImageType)
    }

    enum class ChoosedImageType {
        CameraType,
        GalleryType
    }
}