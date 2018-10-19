package internship.asiantech.a2018summerfinal.database.updater

import internship.asiantech.a2018summerfinal.database.model.Song

interface SongUpdater {
    fun getSongResult(result: List<Song>)
}
