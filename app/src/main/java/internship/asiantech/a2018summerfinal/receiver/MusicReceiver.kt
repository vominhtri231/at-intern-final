package internship.asiantech.a2018summerfinal.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import internship.asiantech.a2018summerfinal.service.MusicPlayerEventListener


/**
 * RTFI
 *
 * receiver = MusicReceiver(
 *      object:MusicPlayerEventListener{
 *          ... functions to update ui here
 *      })
 *
 * filter = IntentFilter().apply{addAction(MusicReceiver.ACTION_UPDATE_RECEIVER)}
 * registerReceiver(receiver, filter)
 *
 */
class MusicReceiver(var listener: MusicPlayerEventListener) : BroadcastReceiver() {

    companion object {
        /**
         * use this constant to apply filter in receiver
         */
        const val ACTION_UPDATE_RECEIVER = "ACTION_UPDATE_RECEIVER"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val letterInfo = Letter.getLetterInfo(intent)
        @Letter.Companion.LetterType
        when (letterInfo.letterType) {
            Letter.START_PLAY_SONG -> listener.onPlayerStart(letterInfo.title, letterInfo.duration)
            Letter.UPDATE_TIME -> listener.onPlayerPlaying(letterInfo.currentTime)
            Letter.PAUSE -> listener.onPlayerPause()
            Letter.UN_PAUSE -> listener.onPlayerUnPause()
            else -> {
                // invalid
            }
        }
    }
}
