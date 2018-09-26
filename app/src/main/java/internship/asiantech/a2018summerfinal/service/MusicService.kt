package internship.asiantech.a2018summerfinal.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.RemoteViews
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.receiver.Letter
import internship.asiantech.a2018summerfinal.ui.PlayMusicActivity
import internship.asiantech.a2018summerfinal.utils.timeToString
import internship.asiantech.a2018summerfinal.utils.trimStringToLength


/**
 * MusicService is used to control the music player
 *
 *
 * To create a music service :
 *     startService(CommandBuilder(context, CommandBuilder.START_SERVICE).build())
 *
 *
 * There are 2 way to interact with music service:(after you create one)
 *  1.Using command :
 *    startService(CommandBuilder(context, CommandBuilder.PLAY_OR_PAUSE).build())
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
 *    bindService(command, serviceConnection, BIND_AUTO_CREATE)
 *    we use musicPlayer here...
 *
 */
class MusicService : Service() {
    private lateinit var binder: MusicBinder
    private lateinit var musicPlayer: MusicPlayer
    private lateinit var remoteView: RemoteViews
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        createMusicPlayer()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        builder = NotificationCompat.Builder(applicationContext, CHANEL_ID)
        remoteView = RemoteViews(packageName, R.layout.remoteview_playingmusic)
        binder = MusicBinder(musicPlayer)
        initRemoteView()
        initBuilder()
    }

    private fun initBuilder() {
        val notificationIntent = Intent(this, PlayMusicActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)

        builder.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCustomBigContentView(remoteView)
                .setSmallIcon(R.drawable.ic_like)
                .setOngoing(true)
                .setAutoCancel(false)
    }

    private fun initRemoteView() {
        val playPendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                CommandBuilder(applicationContext, CommandBuilder.PLAY_OR_PAUSE).build(), 0)

        val deletePendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                CommandBuilder(applicationContext, CommandBuilder.STOP_SERVICE).build(), 0)

        val playNextPendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                CommandBuilder(applicationContext, CommandBuilder.PLAY_NEXT).build(), 0)

        val playPrePendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                CommandBuilder(applicationContext, CommandBuilder.PLAY_PREV).build(), 0)

        remoteView.apply {
            setOnClickPendingIntent(R.id.btnExitMinibar, deletePendingIntent)
            setOnClickPendingIntent(R.id.btnPlayMiniBar, playPendingIntent)
            setOnClickPendingIntent(R.id.btnNextMiniBar, playNextPendingIntent)
            setOnClickPendingIntent(R.id.btnPreviousPlayMiniBar, playPrePendingIntent)
        }
    }

    private fun createMusicPlayer() {
        musicPlayer = MusicPlayer(applicationContext, object : MusicPlayerEventListener {
            override fun onPlayerStart(title: String, duration: Int) {
                remoteView.setTextViewText(R.id.tvNameSongMiniBar,
                        trimStringToLength(title, 18))
                remoteView.setProgressBar(R.id.progressBarMiniBar, duration, 0, false)
                remoteView.setTextViewText(R.id.tvTotalTimeMiniBar, timeToString(duration))
                updatePlay()
                Letter(applicationContext, Letter.START_PLAY_SONG)
                        .title(title).duration(duration).send()
            }

            override fun onPlayerPlaying(time: Int) {
                remoteView.setTextViewText(R.id.tvRunningTimeMiniBar, timeToString(time))
                remoteView.setInt(R.id.progressBarMiniBar, "setProgress", time)
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
                remoteView.setInt(R.id.btnPlayMiniBar, "setBackgroundResource", R.drawable.ic_pause)
                remoteView.setViewVisibility(R.id.btnExitMinibar, View.GONE)
                changeNotification()
            }

            private fun updatePause() {
                remoteView.setInt(R.id.btnPlayMiniBar, "setBackgroundResource", R.drawable.ic_play)
                remoteView.setViewVisibility(R.id.btnExitMinibar, View.VISIBLE)
                changeNotification()
            }

            private fun changeNotification() {
                notificationManager.notify(SERVICE_ID, builder.build())
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            @CommandBuilder.Companion.Command val command: String = CommandBuilder.getCommand(intent)
            @CommandBuilder.Companion.Command
            when (command) {
                CommandBuilder.START_SERVICE -> createForegroundService()
                CommandBuilder.STOP_SERVICE -> endService()
                CommandBuilder.PLAY_OR_PAUSE -> musicPlayer.changeState()
                CommandBuilder.PLAY_NEXT -> musicPlayer.nextSong()
                CommandBuilder.PLAY_PREV -> musicPlayer.previousSong()
            }
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun createForegroundService() {
        val notification = builder.build()
        startForeground(SERVICE_ID, notification)
    }

    private fun endService() {
        stopForeground(true)
        stopSelf()
    }

    companion object {
        private const val SERVICE_ID = 11
        private const val CHANEL_ID = "CHANEL_ID"
    }
}
