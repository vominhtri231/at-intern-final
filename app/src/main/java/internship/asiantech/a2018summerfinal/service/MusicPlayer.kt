package internship.asiantech.a2018summerfinal.service

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import internship.asiantech.a2018summerfinal.model.Music
import java.lang.ref.WeakReference
import java.util.*

/**
 * Music player to play list music
 *
 * @param context where music player is called
 * @param listener how context handle ui according to music player
 */
class MusicPlayer(private val context: Context, private val listener: MusicPlayerEventListener) {
    private var musics: MutableList<Music> = mutableListOf()
    private var isPaused = false
    private var currentPosition = 0
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var timeUpdater: TimeUpdater? = null

    /**
     * set list music
     *
     * @param musics input songs
     */
    fun setMusicList(musics: MutableList<Music>) {
        this.musics = musics
    }

    /**
     * set play next song mode
     */
    fun setPlayNextMode() {
        mediaPlayer.setOnCompletionListener { _ ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            nextSong()
        }
    }

    /**
     * set play random song mode
     */
    fun setPlayRandomMode() {
        mediaPlayer.setOnCompletionListener { _ ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            playAt(Random().nextInt(musics.size))
        }
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
        timeUpdater?.stopUpdate()
        listener.onPlayerPause()
    }

    private fun play() {
        if (!isPaused && !mediaPlayer.isPlaying) {
            if (musics.size > 0) {
                val song = musics[currentPosition]
                val id = song.songId
                val trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                mediaPlayer.setDataSource(context, trackUri)
                mediaPlayer.prepare()
                mediaPlayer.start()
                listener.onPlayerStart(song.songTitle, mediaPlayer.duration)
            }
        } else {
            mediaPlayer.start()
            listener.onPlayerUnPause()
        }
        isPaused = false
        timeUpdater = TimeUpdater(this)
        timeUpdater?.start()
    }

    /**
     * play song at particular position
     *
     * @param position position of song in list song
     */
    fun playAt(position: Int) {
        if (position >= 0 && position < musics.size) {
            timeUpdater?.stopUpdate()
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
        timeUpdater?.stopUpdate()
        if (isPaused) {
            isPaused = false
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        currentPosition = (currentPosition + 1) % musics.size
        play()
    }

    /**
     * play previous song in list song
     */
    fun previousSong() {
        timeUpdater?.stopUpdate()
        if (isPaused) {
            isPaused = false
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        currentPosition--
        if (currentPosition < 0) {
            currentPosition += musics.size
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
        timeUpdater = TimeUpdater(this)
        timeUpdater?.start()
    }

    /**
     * @return playing song's time in millisecond
     */
    private fun getSeekPosition(): Int = mediaPlayer.currentPosition

    /**
     * notify song's information including name and duration
     */
    fun transferPlayingSongInfo() {
        listener.onPlayerStart(musics[currentPosition].songTitle,
                mediaPlayer.duration)

    }

    private class TimeUpdater(musicPlayer: MusicPlayer) : Thread() {
        var musicPlayerReference: WeakReference<MusicPlayer> = WeakReference(musicPlayer)
        private var isRun: Boolean = true

        override fun run() {
            while (isRun) {
                musicPlayerReference.get()?.let {
                    it.listener.onPlayerPlaying(it.getSeekPosition())
                }
                Thread.sleep(50)
            }
        }

        internal fun stopUpdate() {
            isRun = false
        }
    }
}
