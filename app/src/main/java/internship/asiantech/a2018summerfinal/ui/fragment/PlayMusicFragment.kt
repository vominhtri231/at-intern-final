package internship.asiantech.a2018summerfinal.ui.fragment

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.receiver.MusicReceiver
import internship.asiantech.a2018summerfinal.service.CommandBuilder
import internship.asiantech.a2018summerfinal.service.MusicBinder
import internship.asiantech.a2018summerfinal.service.MusicPlayer
import internship.asiantech.a2018summerfinal.service.MusicPlayerEventListener
import internship.asiantech.a2018summerfinal.ui.activity.MainActivity
import internship.asiantech.a2018summerfinal.ui.fragment.listener.AdditionFragmentActionListener
import internship.asiantech.a2018summerfinal.utils.timeToString
import kotlinx.android.synthetic.main.fragment_play_music.*


class PlayMusicFragment : Fragment() {
    private val musicReceiver: MusicReceiver = MusicReceiver(object : MusicPlayerEventListener {
        override fun onPlayerStart(title: String, duration: Int) {
            tvSongName.text = title
            tvTotalTime.text = timeToString(duration)
            tvRunningTime.text = timeToString(duration)
            seekBar.max = duration
            seekBar.progress = 0
            onPlayerUnPause()
        }

        override fun onPlayerPlaying(time: Int) {
            seekBar.progress = time
            tvRunningTime.text = timeToString(time)
        }

        override fun onPlayerPause() {
            imgPlayOrPauseSong.setImageResource(R.drawable.ic_play)
            imgSong.isRunning = false
        }

        override fun onPlayerUnPause() {
            imgPlayOrPauseSong.setImageResource(R.drawable.ic_pause)
            imgSong.isRunning = true
        }
    })

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as MusicBinder
            musicPlayer = binder.getMusicPlayer()?.apply {
                setMusicList(songs)
                transferPlayerState()
            }
            arguments?.getLong(SONG_ID_KEY)?.let {
                musicPlayer?.playWithSongId(it)
            }
        }
    }

    private var musicPlayer: MusicPlayer? = null
    private var backListener: AdditionFragmentActionListener? = null
    private val songs: MutableList<Song> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSongs()
        initService()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AdditionFragmentActionListener) {
            backListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement AdditionFragmentActionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply { addAction(MusicReceiver.ACTION_UPDATE_RECEIVER) }
        activity?.registerReceiver(musicReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(musicReceiver)
    }

    override fun onDestroy() {
        activity?.unbindService(serviceConnection)
        super.onDestroy()
    }

    private fun initView() {
        btnToolBarButtonBack.setOnClickListener {
            backListener?.onBackToStandard()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                musicPlayer?.stopUpdateTime()
            }

            override fun onStopTrackingTouch(p0: SeekBar) {
                musicPlayer?.setSeekPosition(p0.progress)
            }
        })
        imgPlayOrPauseSong.setOnClickListener {
            musicPlayer?.changeState()
        }
        imgPreviousSong.setOnClickListener {
            musicPlayer?.previousSong()
        }
        imgNextSong.setOnClickListener {
            musicPlayer?.nextSong()
        }
        imgModeRepeat?.setOnClickListener {
            musicPlayer?.setPlayMode(MusicPlayer.MODE_REPEAT)
        }
        imgModeShuffle?.setOnClickListener {
            musicPlayer?.setPlayMode(MusicPlayer.MODE_SHUFFLE)
        }
    }

    private fun initSongs() {
        songs.clear()
        songs.addAll((activity as MainActivity).getSongs())
    }

    private fun initService() {
        val command: Intent = CommandBuilder(context, CommandBuilder.START_SERVICE).build()
        activity?.startService(command)
        activity?.bindService(command, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    companion object {
        private const val SONG_ID_KEY = "SONG_ID_KEY"
        @JvmStatic
        fun newInstance(songId: Long) =
                PlayMusicFragment().apply {
                    arguments = Bundle().apply {
                        this.putLong(SONG_ID_KEY, songId)
                    }
                }
    }
}
