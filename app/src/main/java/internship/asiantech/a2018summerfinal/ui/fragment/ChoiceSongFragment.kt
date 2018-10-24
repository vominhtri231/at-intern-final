package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.recyclerview.adapter.PlaylistChoiceAdapter
import internship.asiantech.a2018summerfinal.ui.recyclerview.listener.RadioButtonEventChoice
import kotlinx.android.synthetic.main.fragment_choice_song.*


class ChoiceSongFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val listSongInDevices: MutableList<Song> = mutableListOf()
    private var mListBoolChoice = mutableListOf<Boolean>()
    private lateinit var mAdapterChoiceSong: PlaylistChoiceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choice_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    private fun initViews() {
        listSongInDevices.addAll((activity as MainActivity).getSongs())
        for (index in listSongInDevices.indices) {
            mListBoolChoice.add(false)
        }
        mAdapterChoiceSong = PlaylistChoiceAdapter(mListBoolChoice, listSongInDevices, object : RadioButtonEventChoice {
            override fun onRadioButtonClickListener(position: Int, ischeck: Boolean) {
                val music = listSongInDevices.get(position)
                mListBoolChoice[position] = !mListBoolChoice[position]
                mAdapterChoiceSong.notifyDataSetChanged()
            }
        })
        recycleViewPlaylistChoice.setHasFixedSize(true)
        recycleViewPlaylistChoice.layoutManager = LinearLayoutManager(context)
        recycleViewPlaylistChoice.adapter = mAdapterChoiceSong
    }

    private fun setListener() {
        btnToolBarButtonDown.setOnClickListener {
        }
        rbItemPlaylistChoiceAll.setOnClickListener {
//            mIsCheckAll = !mIsCheckAll
//            rbItemPlaylistChoiceAll.isChecked = mIsCheckAll
//            if (!mIsCheckAll) {
//                mTvStateChoiceAll.text = resources.getString(R.string.choiceAllSOng)
//            } else {
//                mTvStateChoiceAll.text = resources.getString(R.string.exit_choice_all)
//            }
//            choiceAllItem(mIsCheckAll)
        }

        imgbPlaylistChoiceComplete.setOnClickListener {
//            val namePlayList = intent.getStringExtra(PlaylistFragment.KEY_ID_PLAYLIST_BUNDLE)
//            for (song in getListChoice()) {
//                AppDataHelper.getInstance(context).addSong(song)
//                AppDataHelper.getInstance(context).addSongToPlaylist(namePlayList, song.id)
//            }
//            onFinish()
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
        val listChoicedSong = mutableListOf<Song>()
        listSongInDevices.forEachIndexed { index, song ->
            if (mListBoolChoice[index]) {
                listChoicedSong.add(song)
            }
        }
        return listChoicedSong
    }

    companion object {
        const val KEY_POSITION = "key_position"
    }
}
