package internship.asiantech.a2018summerfinal.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import internship.asiantech.a2018summerfinal.ui.fragment.PlaylistFragment
import internship.asiantech.a2018summerfinal.ui.fragment.ListSongsFragment

class LibraryPagerAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager) {
    private val tabTitles = arrayOf("SONG", "ALBUM", "FAVOURITE", "HISTORY")
    private val listFragment: MutableList<Fragment> = mutableListOf()

    init {
        addFragment()
    }

    private fun addFragment() {
        listFragment.add(ListSongsFragment.instance(0))
        listFragment.add(PlaylistFragment())
        listFragment.add(ListSongsFragment.instance(1))
        listFragment.add(ListSongsFragment.instance(2))
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
