package internship.asiantech.a2018summerfinal.firebase

interface StorageUpdater {
    fun onFail()
    fun onSuccess(path: String)
}
