package internship.asiantech.a2018summerfinal.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.DrawerLayoutAdapter
import internship.asiantech.a2018summerfinal.adapter.LibraryPagerAdapter
import internship.asiantech.a2018summerfinal.model.MenuItem
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.sharepreference.UserSharePreference
import kotlinx.android.synthetic.main.activity_list_music.*

class ListMusicActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private var mLibraryPagerAdapter: LibraryPagerAdapter = LibraryPagerAdapter(supportFragmentManager)
    private lateinit var drawerLayoutAdapter: DrawerLayoutAdapter
    private val menuItems: List<MenuItem> = ArrayList()
    private val users: MutableList<User> = ArrayList()
    private var isSearch: Boolean = false

    companion object {
        const val KEY_SEARCH = "search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        initViews()
        initViewPager()
        initRecyclerView()
        btnToolBarButtonSearch.setOnClickListener {
            if (!isSearch) {
                tvToolBarName.visibility = View.GONE
                edtSearch.visibility = View.VISIBLE
                isSearch = true
            } else {
                if (edtSearch.text.toString() != "") {
                    val intent = Intent(this, SearchedActivity::class.java)
                    intent.putExtra(KEY_SEARCH, edtSearch.text.toString())
                    startActivity(intent)
                    isSearch = false
                }
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerViewMenu.layoutManager = layoutManager
        val userSharedPreferences = UserSharePreference(this)
        val user = userSharedPreferences.getCurrentUser()
        user?.let {
            users.add(user)
        }
        drawerLayoutAdapter = DrawerLayoutAdapter(menuItems, users, this) {
            userSharedPreferences.removeUserCurrent()
        }
        recyclerViewMenu.adapter = drawerLayoutAdapter
    }

    private fun initViews() {
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
    }

    private fun initViewPager() {
        mViewPager.adapter = mLibraryPagerAdapter
        mViewPager.currentItem
        mTabLayout.setupWithViewPager(mViewPager)
    }

}
