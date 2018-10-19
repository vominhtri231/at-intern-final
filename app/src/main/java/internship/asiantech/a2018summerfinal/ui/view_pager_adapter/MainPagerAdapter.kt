package internship.asiantech.a2018summerfinal.ui.view_pager_adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import internship.asiantech.a2018summerfinal.ui.fragment.LibraryFragment
import internship.asiantech.a2018summerfinal.ui.fragment.ListSongFragment
import internship.asiantech.a2018summerfinal.ui.fragment.PlaylistFragment

class MainPagerAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager) {
    private val tabTitles = arrayOf("SONG", "PLAYLIST", "LIBRARY")
    private val listFragment: MutableList<Fragment> = mutableListOf()

    init {
        addFragment()
    }

    private fun addFragment() {
        listFragment.add(ListSongFragment.instance(ListSongFragment.TYPE_ALL))
        listFragment.add(PlaylistFragment())
        listFragment.add(LibraryFragment())
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}
