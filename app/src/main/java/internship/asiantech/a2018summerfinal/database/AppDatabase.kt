package internship.asiantech.a2018summerfinal.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import internship.asiantech.a2018summerfinal.database.dao.PlaylistDAO
import internship.asiantech.a2018summerfinal.database.dao.PlaylistSongDAO
import internship.asiantech.a2018summerfinal.database.dao.SongDAO
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.PlaylistSong
import internship.asiantech.a2018summerfinal.database.model.Song


@Database(
        entities = [(Song::class), (Playlist::class), (PlaylistSong::class)],
        version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDAO(): SongDAO
    abstract fun playlistDAO(): PlaylistDAO
    abstract fun playlistSongDAO(): PlaylistSongDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        internal fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "music_app.db")
                        .build()
    }
}
