package internship.asiantech.a2018summerfinal.ui.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LibraryEventListener
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.LibraryAdapter
import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment : Fragment() {
    private lateinit var listener: LibraryEventListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LibraryEventListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement LibraryEventListener")
        }
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = LibraryAdapter(context, listener)
    }
}
