package internship.asiantech.a2018summerfinal.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import internship.asiantech.a2018summerfinal.ui.fragment.PlaylistFragment
import internship.asiantech.a2018summerfinal.ui.fragment.ListSongsFragment


class LibraryPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private companion object {
        val TAB_TITLES = arrayOf("BÀI HÁT", "ALBUM", "FAVOURITE", "HISTORY")
    }

    private val mListFragment: MutableList<Fragment> = mutableListOf()

    init {
        addFragment()
    }

    private fun addFragment() {
        mListFragment.add(ListSongsFragment.instance(0))
        mListFragment.add(PlaylistFragment())
        mListFragment.add(ListSongsFragment.instance(1))
        mListFragment.add(ListSongsFragment.instance(2))
    }

    override fun getItem(position: Int): Fragment {
        return mListFragment[position]
    }

    override fun getCount(): Int {
        return mListFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }
}
