package internship.asiantech.a2018summerfinal.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.utils.showKeyboard

class LibraryStandardToolbarFragment : Fragment() {
    private lateinit var listener: StandardToolbarEventListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_standard_toolbar, container, false).also {
            setListeners(it)
            showKeyboard(activity as Activity)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is StandardToolbarEventListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement StandardToolbarEventListener")
        }
    }

    private fun setListeners(view: View) {
        val bttStartSearch = view.findViewById<Button>(R.id.btnToolBarButtonSearch)
        bttStartSearch.setOnClickListener {
            listener.onStartSearch()
            showKeyboard(activity as Activity)
        }
        val imgUser = view.findViewById<ImageView>(R.id.imgAvatar)
        imgUser.setOnClickListener {
            listener.onViewUserInfo()
        }
    }
}
