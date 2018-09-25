package internship.asiantech.a2018summerfinal.service

import android.os.Binder
import java.lang.ref.WeakReference

class MusicBinder(musicPlayer: MusicPlayer) : Binder() {
    private val musicPlayerReference: WeakReference<MusicPlayer> = WeakReference(musicPlayer)
    fun getMusicPlayer(): MusicPlayer? = musicPlayerReference.get()
}
