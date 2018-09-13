package internship.asiantech.a2018summerfinal

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import internship.asiantech.a2018summerfinal.adapter.PlaylistChoiceAdapter
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.fragment.PlaylistFragment

class ChoiceSongActivity : AppCompatActivity() {
    private lateinit var mImgbPlaylistChoiceComplete: ImageButton
    private lateinit var mRbItemPlaylistChoiceAll: RadioButton
    private lateinit var mBtnToolBarButtonDown: Button
    private lateinit var mTvStateChoiceAll: TextView
    private lateinit var mRecyclerviewPlaylistChoiceSong: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var listSongInDevices: MutableList<Song>
    private lateinit var listPlaylist: MutableList<Playlist>
    private lateinit var mContext: Context
    val KEY_POSITION = "key_position"

    private var mIsCheckAll = false
    private var mAdapterChoiceSong = PlaylistChoiceAdapter(listSongInDevices)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_song)
        initViews()
        setListener()
    }

    private fun initViews() {
        mTvStateChoiceAll = findViewById(R.id.tvStateChoiceAll)
        mImgbPlaylistChoiceComplete = findViewById(R.id.imgbPlaylistChoiceComplete)
        mRbItemPlaylistChoiceAll = findViewById(R.id.rbItemPlaylistChoiceAll)
        mRecyclerviewPlaylistChoiceSong = findViewById(R.id.recyclerviewPlaylist)
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerviewPlaylistChoiceSong.layoutManager = mLinearLayoutManager
        mRecyclerviewPlaylistChoiceSong.setHasFixedSize(true)
        mAdapterChoiceSong = PlaylistChoiceAdapter(listSongInDevices)
        mRecyclerviewPlaylistChoiceSong.adapter = mAdapterChoiceSong
    }

    private fun setListener() {
        val intent = getIntent()
        var position = intent.getIntExtra(KEY_POSITION,0)
        mBtnToolBarButtonDown.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        mRbItemPlaylistChoiceAll.setOnClickListener(View.OnClickListener {
            mIsCheckAll = !mIsCheckAll
            if (!mIsCheckAll) {
                mTvStateChoiceAll.text = resources.getString(R.string.choiceAllSOng)
            } else {
                mTvStateChoiceAll.text = resources.getString(R.string.exit_choice_all)
            }
            mAdapterChoiceSong.choiceAllItem(mIsCheckAll,position)
        })
//        mImgbPlaylistChoiceComplete.setOnClickListener(View.OnClickListener {
//            AppDataHelper.getInstance(this).addSongToPlaylist(,)
//        })
    }
}
