package internship.asiantech.a2018summerfinal.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.receiver.MusicReceiver
import internship.asiantech.a2018summerfinal.service.CommandBuilder
import internship.asiantech.a2018summerfinal.service.MusicBinder
import internship.asiantech.a2018summerfinal.service.MusicPlayer
import internship.asiantech.a2018summerfinal.service.MusicPlayerEventListener
import internship.asiantech.a2018summerfinal.utils.askForPermissions
import internship.asiantech.a2018summerfinal.utils.querySongs
import internship.asiantech.a2018summerfinal.utils.timeToString
import kotlinx.android.synthetic.main.activity_play_music.*

class PlayMusicActivity : AppCompatActivity() {
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
        }
    }

    private var musicPlayer: MusicPlayer? = null
    private val songs: MutableList<Song> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        initView()

        if (askForPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_REQUEST_CODE)) {
            songs.addAll(querySongs(contentResolver))
            initService()
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply { addAction(MusicReceiver.ACTION_UPDATE_RECEIVER) }
        registerReceiver(musicReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(musicReceiver)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == READ_EXTERNAL_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            songs.addAll(querySongs(contentResolver))
            initService()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initView() {
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

    private fun initService() {
        val command: Intent = CommandBuilder(this, CommandBuilder.START_SERVICE).build()
        startService(command)
        bindService(command, serviceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        const val READ_EXTERNAL_REQUEST_CODE = 231
    }
}
