package internship.asiantech.a2018summerfinal.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import internship.asiantech.a2018summerfinal.database.DAO.PlaylistDAO
import internship.asiantech.a2018summerfinal.database.DAO.PlaylistSongDAO
import internship.asiantech.a2018summerfinal.database.DAO.SongDAO
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.PlaylistSong
import internship.asiantech.a2018summerfinal.database.model.Song

@Database(
        entities = arrayOf(Song::class, Playlist::class, PlaylistSong::class),
        version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDAO(): SongDAO
    abstract fun playlistDAO(): PlaylistDAO
    abstract fun playlistSongDAO(): PlaylistSongDAO
}
