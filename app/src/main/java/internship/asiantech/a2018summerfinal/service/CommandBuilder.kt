package internship.asiantech.a2018summerfinal.service

import android.content.Context
import android.content.Intent
import android.support.annotation.StringDef

/**
 *
 * CommandBuilder is a handmade Intent for MusicService
 *
 * how to control music player via command:
 *      startService(CommandBuilder(context,CommandBuilder.PLAY_OR_PAUSE).build())
 */

class CommandBuilder(private val context: Context, @Command private val command: String) {
    fun build(): Intent = Intent(context, MusicService::class.java).apply {
        action = command
    }

    companion object {
        @StringDef(START_SERVICE, STOP_SERVICE, PLAY_OR_PAUSE, PLAY_NEXT, PLAY_PREV)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Command

        const val START_SERVICE = "START_SERVICE"
        const val STOP_SERVICE = "STOP_SERVICE"
        const val PLAY_OR_PAUSE = "PLAY_OR_PAUSE"
        const val PLAY_NEXT = "PLAY_NEXT"
        const val PLAY_PREV = "PLAY_PREV"

        /**
         * To check command type of intent when you receive a command Intent
         */
        internal fun getCommand(intent: Intent): String = intent.action
    }
}
