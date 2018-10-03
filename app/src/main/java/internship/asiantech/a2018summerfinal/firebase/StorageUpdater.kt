package internship.asiantech.a2018summerfinal.firebase

import android.net.Uri

interface StorageUpdater {
    fun onSuccess(uri: Uri)
    fun onFail()
}