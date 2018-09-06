package internship.asiantech.a2018summerfinal.service

interface MusicPlayerEventListener {
    fun onPlayerStart(title: String, duration: Int)
    fun onPlayerPlaying(time: Int)
    fun onPlayerPause()
    fun onPlayerUnPause()
}
