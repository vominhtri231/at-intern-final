package internship.asiantech.a2018summerfinal.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.RemoteViews
import internship.asiantech.a2018summerfinal.MainActivity
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.receiver.Letter
import java.lang.ref.WeakReference


class MusicService : Service() {
    companion object {
        private const val SERVICE_ID = 101
        private const val CHANEL_ID = "CHANEL_ID"
    }

    private var musicPlayer: MusicPlayer = MusicPlayer(applicationContext, object : MusicPlayerEventListener {
        override fun onPlayerStart(title: String, duration: Int) {
            remoteView.setTextViewText(R.id.tvNameSongMiniBar, title)
            updatePlay()
            Letter(applicationContext, Letter.START_PLAY_SONG)
                    .title(title).duration(duration).send()
        }

        override fun onPlayerPlaying(time: Int) {
            remoteView.setTextViewText(R.id.tvRunningTimeMiniBar, time.toString())
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
            remoteView.setImageViewResource(R.id.btnPlayMiniBar, R.drawable.ic_pause)
            remoteView.setViewVisibility(R.id.btnExitMinibar, View.INVISIBLE)
            changeNotification()
        }

        private fun updatePause() {
            remoteView.setImageViewResource(R.id.btnPlayMiniBar, R.drawable.ic_play)
            remoteView.setViewVisibility(R.id.btnExitMinibar, View.VISIBLE)
            changeNotification()
        }

        private fun changeNotification() {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(SERVICE_ID, builder.build())
        }
    })

    private val remoteView: RemoteViews = RemoteViews(packageName, R.layout.remoteview_playingmusic)

    private val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANEL_ID)
            .setCustomBigContentView(remoteView)
            .setSmallIcon(R.drawable.ic_play)
            .setOngoing(true)

    init {
        val playPendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                Command(applicationContext, Command.PLAY_OR_PAUSE).build(), 0)
        val deletePendingIntent: PendingIntent = PendingIntent.getService(this, 0,
                Command(applicationContext, Command.STOP_SERVICE).build(), 0)
        remoteView.apply {
            setOnClickPendingIntent(R.id.btnPlayMiniBar, playPendingIntent)
            setOnClickPendingIntent(R.id.btnExitMinibar, deletePendingIntent)
        }

        val notificationIntent: Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setContentIntent(pendingIntent)
    }

    private val binder = MusicBinder(musicPlayer)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            @Command.Companion.Command val command: Int = Command.getCommand(intent)
            @Command.Companion.Command
            when (command) {
                Command.START_SERVICE -> startForeground(SERVICE_ID, builder.build())
                Command.PLAY_OR_PAUSE -> musicPlayer.changeState()
                Command.STOP_SERVICE -> {
                    stopSelf()
                    stopForeground(true)
                }
                else -> {
                    // invalid
                }
            }
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    class MusicBinder(musicPlayer: MusicPlayer) : Binder() {
        private val musicPlayerReference: WeakReference<MusicPlayer> = WeakReference(musicPlayer)
        val musicPlayer: MusicPlayer?
            get() = musicPlayerReference.get()
    }
}
