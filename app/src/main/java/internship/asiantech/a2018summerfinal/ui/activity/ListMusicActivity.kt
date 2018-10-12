package internship.asiantech.a2018summerfinal.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.LibraryPagerAdapter
import internship.asiantech.a2018summerfinal.ui.fragment.LibrarySearchToolbarFragment
import internship.asiantech.a2018summerfinal.ui.fragment.LibraryStandardToolbarFragment
import internship.asiantech.a2018summerfinal.ui.fragment.SearchToolbarEventListener
import internship.asiantech.a2018summerfinal.ui.fragment.StandardToolbarEventListener
import kotlinx.android.synthetic.main.activity_list_music.*

class ListMusicActivity : AppCompatActivity()
        , StandardToolbarEventListener, SearchToolbarEventListener {
    private lateinit var libraryPagerAdapter: LibraryPagerAdapter

    companion object {
        private val TAG = ListMusicActivity::class.qualifiedName
        const val KEY_SEARCH = "search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        initViewPager()
        initRecyclerView()
        supportFragmentManager.beginTransaction()
                .add(R.id.flToolbar, LibraryStandardToolbarFragment()).commit()
    }

    private fun initRecyclerView() {
        //TODO : init recycler view that contain user's information
    }

    private fun initViewPager() {
        libraryPagerAdapter = LibraryPagerAdapter(supportFragmentManager)
        viewPager.adapter = libraryPagerAdapter
        viewPager.currentItem
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onStartSearch() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flToolbar, LibrarySearchToolbarFragment()).commit()
    }

    override fun onViewUserInfo() {
        drawerLayout.openDrawer(Gravity.END)
    }

    override fun onSearch(input: String) {
        Log.e(TAG, input)
    }

    override fun onBack() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flToolbar, LibraryStandardToolbarFragment()).commit()
    }
}
