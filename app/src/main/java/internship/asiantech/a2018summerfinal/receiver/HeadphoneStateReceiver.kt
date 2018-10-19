package internship.asiantech.a2018summerfinal.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HeadphoneStateReceiver(private val changeListener: HeadphoneChanger?) : BroadcastReceiver() {

    companion object {
        const val ERROR = -1
        const val KEY_HEADPHONE_STATE = "state"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val state = intent.getIntExtra(KEY_HEADPHONE_STATE, ERROR)
            changeListener?.onCommand(state)
        }
    }
}
