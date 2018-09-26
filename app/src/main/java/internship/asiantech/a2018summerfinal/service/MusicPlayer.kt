package internship.asiantech.a2018summerfinal.service

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.support.annotation.IntDef
import internship.asiantech.a2018summerfinal.database.model.Song
import java.lang.ref.WeakReference
import java.util.*

/**
 * Music player to play list music
 *
 * @param context where music player is called
 * @param listener how context handle ui according to music player
 */
class MusicPlayer(private val context: Context, private val listener: MusicPlayerEventListener) {
    private var songs: MutableList<Song> = mutableListOf()
    private var isPaused = false
    @Mode
    private var mode: Int = MODE_NEXT
    private var currentPosition = 0
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setOnCompletionListener { _ ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            @Mode
            when (mode) {
                MODE_NEXT -> nextSong()
                MODE_SHUFFLE -> playAt(Random().nextInt(songs.size))
                MODE_REPEAT -> playAt(currentPosition)
            }
        }
    }

    private var timeUpdater: TimeUpdater? = null

    /**
     * set list music
     *
     * @param songs input songs
     */
    fun setMusicList(songs: List<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
    }


    /**
     * set play song mode
     */
    fun setPlayMode(@Mode mode: Int) {
        this.mode = mode
    }

    /**
     * change state of player from pause to playing and reverse
     */
    fun changeState() {
        if (isPaused || !mediaPlayer.isPlaying) {
            play()
        } else {
            pause()
        }
    }

    private fun pause() {
        if (!isPaused) {
            mediaPlayer.pause()
            isPaused = true
        }
        stopUpdateTime()
        listener.onPlayerPause()
    }

    private fun play() {
        if (!isPaused && !mediaPlayer.isPlaying) {
            if (songs.size > 0) {
                val song = songs[currentPosition]
                val id = song.id
                val trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                mediaPlayer.setDataSource(context, trackUri)
                mediaPlayer.prepare()
                mediaPlayer.start()
                listener.onPlayerStart(song.title, mediaPlayer.duration)
            }
        } else {
            mediaPlayer.start()
            listener.onPlayerUnPause()
        }
        isPaused = false
        startUpdateTime()
    }

    /**
     * play song at particular position
     *
     * @param position position of song in list song
     */
    private fun playAt(position: Int) {
        if (position >= 0 && position < songs.size) {
            stopUpdateTime()
            if (isPaused) {
                isPaused = false
            }
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            currentPosition = position
            play()
        }
    }

    /**
     * play next song in list song
     */
    fun nextSong() {
        stopUpdateTime()
        if (songs.size == 0) {
            return
        }
        if (isPaused) {
            isPaused = false
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        currentPosition = (currentPosition + 1) % songs.size
        play()
    }

    /**
     * play previous song in list song
     */
    fun previousSong() {
        stopUpdateTime()
        if (isPaused) {
            isPaused = false
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        currentPosition--
        if (currentPosition < 0) {
            currentPosition += songs.size
        }
        play()
    }

    /**
     * seek playing song to a particular millisecond
     *
     * @param millisecond time to seek to
     */
    fun setSeekPosition(millisecond: Int) {
        mediaPlayer.seekTo(millisecond)
        startUpdateTime()
    }

    private fun getSeekPosition(): Int = mediaPlayer.currentPosition

    /**
     * notify song's information including title and duration
     */
    fun transferPlayerState() {
        if (isPaused || mediaPlayer.isPlaying) {
            listener.onPlayerPlaying(mediaPlayer.currentPosition)
            listener.onPlayerStart(songs[currentPosition].title,
                    mediaPlayer.duration)
        }
        if (isPaused || !mediaPlayer.isPlaying) {
            listener.onPlayerPause()
        } else {
            listener.onPlayerUnPause()
        }
    }

    /**
     * stop the timer
     */
    fun stopUpdateTime() {
        timeUpdater?.stopUpdate()
    }

    private fun startUpdateTime() {
        timeUpdater?.stopUpdate()
        timeUpdater = TimeUpdater(this)
        timeUpdater?.start()
    }

    companion object {
        @IntDef(MODE_REPEAT, MODE_SHUFFLE, MODE_NEXT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Mode

        const val MODE_REPEAT = 0
        const val MODE_SHUFFLE = 1
        const val MODE_NEXT = 2
    }

    private class TimeUpdater(musicPlayer: MusicPlayer) : Thread() {
        var musicPlayerReference: WeakReference<MusicPlayer> = WeakReference(musicPlayer)
        private var isRun: Boolean = true

        override fun run() {
            while (isRun) {
                musicPlayerReference.get()?.let {
                    it.listener.onPlayerPlaying(it.getSeekPosition())
                }
                Thread.sleep(100)
            }
        }

        internal fun stopUpdate() {
            isRun = false
        }
    }
}
