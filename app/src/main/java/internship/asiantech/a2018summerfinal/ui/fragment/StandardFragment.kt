package internship.asiantech.a2018summerfinal.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.view_pager_adapter.MainPagerAdapter
import internship.asiantech.a2018summerfinal.ui.fragment.listener.StandardFragmentActionListener
import internship.asiantech.a2018summerfinal.utils.showKeyboard
import kotlinx.android.synthetic.main.fragment_standard.*

class StandardFragment : Fragment() {
    private lateinit var listener: StandardFragmentActionListener
    private lateinit var libraryPagerAdapter: MainPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_standard, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is StandardFragmentActionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement StandardFragmentActionListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initViewPager()
        showKeyboard(activity as Activity)
    }

    fun updatePlaylistFragment(){
        val fragment=libraryPagerAdapter.getItem(1)
        if(fragment is PlaylistFragment){
            fragment.getPlayList()
        }
    }

    private fun setListeners() {
        btnToolBarButtonSearch.setOnClickListener {
            listener.onStartSearch()
            showKeyboard(activity as Activity)
        }
        imgAvatar.setOnClickListener {
            listener.onViewUserInfo()
        }
    }

    private fun initViewPager() {
        libraryPagerAdapter = MainPagerAdapter(fragmentManager)
        viewPager.adapter = libraryPagerAdapter
        viewPager.currentItem
        tabLayout.setupWithViewPager(viewPager)
    }
}
