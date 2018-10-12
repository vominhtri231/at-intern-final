package internship.asiantech.a2018summerfinal.ui.fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.utils.hideKeyboard

class LibrarySearchToolbarFragment : Fragment() {
    private lateinit var listener: SearchToolbarEventListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_search_toolbar, container, false).also {
            setListeners(it)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchToolbarEventListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement SearchToolbarEventListener")
        }
    }

    private fun setListeners(view: View) {
        val bttBack = view.findViewById<Button>(R.id.btnToolBarButtonBack)
        bttBack.setOnClickListener {
            listener.onBack()
        }
        val edtSearchInput = view.findViewById<EditText>(R.id.edtSearchInput)
        edtSearchInput.setOnKeyListener(View.OnKeyListener { _, key, keyEvent ->
            if (keyEvent?.action == KeyEvent.ACTION_DOWN
                    && key == KeyEvent.KEYCODE_ENTER) {
                listener.onSearch(edtSearchInput.text.toString())
                edtSearchInput.clearFocus()
                hideKeyboard(activity as Activity)
                return@OnKeyListener true
            }
            false
        })
        edtSearchInput.requestFocus()
    }
}
