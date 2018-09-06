package internship.asiantech.a2018summerfinal.librarysong

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.adapter.LibraryPagerAdapter
import internship.asiantech.a2018summerfinal.listmusic.ListMusic

class ListMusicActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout : TabLayout
    private var mLibraryPagerAdapter : LibraryPagerAdapter = LibraryPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        initViews()
        initViewPager()
        val listMusic=ListMusic(this)
        for (music in listMusic.getListMusics()){
            Log.d("aaa", music.songTitle)
        }
    }
    private fun initViews(){
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
    }
    private fun initViewPager(){
        mViewPager.adapter = mLibraryPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
