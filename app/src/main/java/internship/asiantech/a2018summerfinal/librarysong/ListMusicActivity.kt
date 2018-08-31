package internship.asiantech.a2018summerfinal.librarysong

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.LibraryPagerAdapter

class ListMusicActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout : TabLayout
    private lateinit var mLibraryPagerAdapter : LibraryPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        initViews()
        initViewPager()
    }
    private fun initViews(){
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
    }
    private fun initViewPager(){
        mLibraryPagerAdapter = LibraryPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mLibraryPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
