package internship.asiantech.a2018summerfinal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import internship.asiantech.a2018summerfinal.adapter.PlaylistChoiceAdapter
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.PlaylistFragment
import internship.asiantech.a2018summerfinal.listmusic.ListMusic
import internship.asiantech.a2018summerfinal.viewholder.RadioButtonEventChoice
import kotlinx.android.synthetic.main.activity_choice_song.*

class ChoiceSongActivity : AppCompatActivity() {
    private lateinit var mImgbPlaylistChoiceComplete: ImageButton
    private lateinit var mBtnToolBarButtonDown: Button
    private lateinit var mTvStateChoiceAll: TextView
    private lateinit var mRecyclerviewPlaylistChoiceSong: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var listSongInDevices: MutableList<Song> = mutableListOf()
    private var mListBoolChoice = mutableListOf<Boolean>()
    private var listSongInPlaylist = mutableListOf<Song>()
    private var mIsCheck = false

    companion object {
        val KEY_POSITION = "key_position"
        val RESULT_KEY = "RESULT_KEY"
    }

    private var mIsCheckAll = false
    private lateinit var mAdapterChoiceSong: PlaylistChoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_song)
        initViews()
        setListener()
    }

    private fun initViews() {
        listSongInDevices = ListMusic(this).getListMusics()
        for (index in listSongInDevices.indices) {
            mListBoolChoice.add(false)
        }
        mTvStateChoiceAll = findViewById(R.id.tvStateChoiceAll)
        mImgbPlaylistChoiceComplete = findViewById(R.id.imgbPlaylistChoiceComplete)
        mBtnToolBarButtonDown = findViewById(R.id.btnToolBarButtonDown)
        mRecyclerviewPlaylistChoiceSong = findViewById(R.id.recycleViewPlaylistChoice)
        mAdapterChoiceSong = PlaylistChoiceAdapter(mListBoolChoice, listSongInDevices, object : RadioButtonEventChoice {
            override fun onRadioButtonClickListener(position: Int, ischeck: Boolean) {
                val music = listSongInDevices.get(position)
                mListBoolChoice[position] = !mListBoolChoice[position]
                mAdapterChoiceSong.notifyDataSetChanged()
            }
        })
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerviewPlaylistChoiceSong.setHasFixedSize(true)
        mRecyclerviewPlaylistChoiceSong.layoutManager = mLinearLayoutManager
        mRecyclerviewPlaylistChoiceSong.adapter = mAdapterChoiceSong
    }

    private fun setListener() {
        val intent = intent
        val position = intent.getIntExtra(KEY_POSITION, 0)
        mBtnToolBarButtonDown.setOnClickListener {
            onBackPressed()
        }
        rbItemPlaylistChoiceAll.setOnClickListener {
            mIsCheckAll = !mIsCheckAll
            rbItemPlaylistChoiceAll.isChecked = mIsCheckAll
            if (!mIsCheckAll) {
                mTvStateChoiceAll.text = resources.getString(R.string.choiceAllSOng)
            } else {
                mTvStateChoiceAll.text = resources.getString(R.string.exit_choice_all)
            }
            choiceAllItem(mIsCheckAll)
        }

        imgbPlaylistChoiceComplete.setOnClickListener {
            val namePlayList = intent.getStringExtra(PlaylistFragment.KEY_ID_PLAYLIST_BUNDLE)
            for (song in getListChoice()) {
                AppDataHelper.getInstance(this).addSong(song)
                AppDataHelper.getInstance(this).addSongToPlaylist(namePlayList, song.id)
            }
            finish()
        }
    }

    private fun choiceAllItem(isCheck: Boolean) {
        if (isCheck) {
            for (index in mListBoolChoice.indices) {
                mListBoolChoice[index] = true

            }
        } else {
            for (index in mListBoolChoice.indices) {
                mListBoolChoice[index] = false
            }
        }
        mAdapterChoiceSong.notifyDataSetChanged()
    }

    private fun getListChoice(): MutableList<Song> {
        val mListChoicedSong = mutableListOf<Song>()
        listSongInDevices.forEachIndexed { index, song ->
            if (mListBoolChoice[index]) {
                mListChoicedSong.add(song)
            }
        }
        return mListChoicedSong
    }
}
