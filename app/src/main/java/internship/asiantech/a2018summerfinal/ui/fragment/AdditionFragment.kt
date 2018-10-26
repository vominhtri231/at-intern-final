package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.fragment.listener.AdditionFragmentActionListener
import kotlinx.android.synthetic.main.fragment_addition.*

abstract class AdditionFragment : Fragment() {
    private lateinit var listener: AdditionFragmentActionListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AdditionFragmentActionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ListSongFragmentActionListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDiffViews(layoutInflater)
        initView()
        setListeners()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addition, container, false)
    }

    abstract fun addDiffViews(inflater: LayoutInflater)

    abstract fun initView()

    protected open fun setListeners() {
        btnToolBarButtonBack.setOnClickListener {
            listener.onBackToStandard()
        }
    }
}
