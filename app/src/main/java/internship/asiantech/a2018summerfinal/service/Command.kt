package internship.asiantech.a2018summerfinal.service

import android.content.Context
import android.content.Intent
import android.support.annotation.IntDef

/**
 *
 * Command is a handmade Intent for MusicService
 *
 * how to control music player via command
 * Command(context,Command.PLAY_OR_PAUSE).build()
 */

class Command(private val context: Context, @Command private val command: Int) {
    fun build(): Intent = Intent(context, MusicService::class.java).apply {
        putExtra(COMMAND_KEY, command)
    }

    companion object {
        @IntDef(START_SERVICE, STOP_SERVICE, PLAY_OR_PAUSE, INVALID)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Command

        const val START_SERVICE = 0
        const val STOP_SERVICE = 1
        const val PLAY_OR_PAUSE = 2
        const val INVALID = -1

        private const val COMMAND_KEY = "command key"

        /**
         * To check command type of intent when you receive a command Intent
         */
        @Command
        internal fun getCommand(intent: Intent): Int = intent.getIntExtra(COMMAND_KEY, INVALID)
    }
}
