package internship.asiantech.a2018summerfinal.librarysong

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.DrawerLayoutAdapter
import internship.asiantech.a2018summerfinal.adapter.LibraryPagerAdapter
import internship.asiantech.a2018summerfinal.model.MenuItem
import internship.asiantech.a2018summerfinal.model.User
import kotlinx.android.synthetic.main.activity_list_music.*
import com.google.gson.Gson
import internship.asiantech.a2018summerfinal.ui.LoginActivity


class ListMusicActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private var mLibraryPagerAdapter: LibraryPagerAdapter = LibraryPagerAdapter(supportFragmentManager)
    private lateinit var drawerLayoutAdapter: DrawerLayoutAdapter
    private val menuItems : List<MenuItem> = ArrayList()
    private val users : MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        initViews()
        initViewPager()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager=LinearLayoutManager(this)
        recyclerViewMenu.layoutManager=layoutManager
        val sharedPreferences = getSharedPreferences(FirebaseAnalytics.Event.LOGIN, MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(LoginActivity.USER, "")
        val user = gson.fromJson<User>(json, User::class.java)
        users.add(user)
        drawerLayoutAdapter= DrawerLayoutAdapter(menuItems, users, this)
        recyclerViewMenu.adapter=drawerLayoutAdapter
    }

    private fun initViews() {
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
    }

    private fun initViewPager() {
        mViewPager.adapter = mLibraryPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
