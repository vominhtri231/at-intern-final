package internship.asiantech.a2018summerfinal.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import internship.asiantech.a2018summerfinal.MainActivity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.receiver.Letter


/**
 * MusicService is used to control the music player
 *
 *
 * To create a music service :
 *     startService(Command(context, Command.START_SERVICE).build())
 *
 *
 * There are 2 way to interact with music service:(after you create one)
 *  1.Using Command :
 *    startService(Command(context, Command.PLAY_OR_PAUSE).build())
 *  2.Using binder :
 *
 *    val serviceConnection:ServiceConnection=object :ServiceConnection{
 *      override fun onServiceDisconnected(p0: ComponentName) {
 *
 *      }
 *
 *      override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
 *        val binder = p1 as PlayMusicService.MusicBinder
 *        musicPlayer = binder.getMusicPlayer()
 *      }
 *    }
 *
 *    musicPlayer.init(songs)
 *    musicPlayer.playAt(...)
 *
 */
class MusicService : Service() {
    companion object {
        private const val SERVICE_ID = 101
        private const val CHANEL_ID = "CHANEL_ID"
    }

    private lateinit var musicPlayer: MusicPlayer
    private lateinit var binder: MusicBinder

    private var remoteView: RemoteViews? = null
    private var builder: NotificationCompat.Builder? = null

    override fun onCreate() {
        musicPlayer = MusicPlayer(applicationContext, object : MusicPlayerEventListener {
            override fun onPlayerStart(title: String, duration: Int) {
                remoteView?.setTextViewText(R.id.tvNameSongMiniBar, title)
                updatePlay()
                Letter(applicationContext, Letter.START_PLAY_SONG)
                        .title(title).duration(duration).send()
            }

            override fun onPlayerPlaying(time: Int) {
                remoteView?.setTextViewText(R.id.tvRunningTimeMiniBar, time.toString())
                changeNotification()
                Letter(applicationContext, Letter.UPDATE_TIME)
                        .currentTime(time).send()
            }

            override fun onPlayerPause() {
                updatePause()
                Letter(applicationContext, Letter.PAUSE).send()
            }

            override fun onPlayerUnPause() {
                updatePlay()
                Letter(applicationContext, Letter.UN_PAUSE).send()
            }

            private fun updatePlay() {
                remoteView?.setImageViewResource(R.id.btnPlayMiniBar, R.drawable.ic_pause)
                remoteView?.setViewVisibility(R.id.btnExitMinibar, View.INVISIBLE)
                changeNotification()
            }

            private fun updatePause() {
                remoteView?.setImageViewResource(R.id.btnPlayMiniBar, R.drawable.ic_play)
                remoteView?.setViewVisibility(R.id.btnExitMinibar, View.VISIBLE)
                changeNotification()
            }

            private fun changeNotification() {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(SERVICE_ID, builder?.build())
            }
        })

        binder = MusicBinder(musicPlayer)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            @Command.Companion.Command val command: Int = Command.getCommand(intent)
            @Command.Companion.Command
            when (command) {
                Command.START_SERVICE -> createForegroundService()
                Command.PLAY_OR_PAUSE -> musicPlayer.changeState()
                Command.STOP_SERVICE -> endService()
            }
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun createForegroundService() {
        remoteView = RemoteViews(packageName, R.layout.remoteview_playingmusic)
        builder = NotificationCompat.Builder(applicationContext, CHANEL_ID)
                .setCustomBigContentView(remoteView)
                .setSmallIcon(R.drawable.ic_like)
                .setOngoing(true)

        val playPendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                Command(applicationContext, Command.PLAY_OR_PAUSE).build(), 0)
        val deletePendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                Command(applicationContext, Command.STOP_SERVICE).build(), 0)
        remoteView?.apply {
            setOnClickPendingIntent(R.id.btnPlayMiniBar, playPendingIntent)
            setOnClickPendingIntent(R.id.btnExitMinibar, deletePendingIntent)
        }

        val notificationIntent: Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder?.setContentIntent(pendingIntent)

        startForeground(SERVICE_ID, builder?.build())
    }

    private fun endService() {
        stopSelf()
        stopForeground(true)
    }
}
