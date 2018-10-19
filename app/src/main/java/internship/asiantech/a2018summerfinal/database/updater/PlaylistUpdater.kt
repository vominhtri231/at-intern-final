package internship.asiantech.a2018summerfinal.database.updater

import internship.asiantech.a2018summerfinal.database.model.Playlist

interface PlaylistUpdater {
    fun getPlaylistResult(result: List<Playlist>)
}
