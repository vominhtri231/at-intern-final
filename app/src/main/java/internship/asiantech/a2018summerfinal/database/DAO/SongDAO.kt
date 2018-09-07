package internship.asiantech.a2018summerfinal.database.DAO

import android.arch.persistence.room.*
import internship.asiantech.a2018summerfinal.database.model.Song

@Dao
interface SongDAO {

    @Query("Select * from song")
    fun getAllSong(): List<Song>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAllSong(songs: List<Song>)

    @Query("Delete from song where id= :id")
    fun deleteSongWithId(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSong(song: Song)
}
