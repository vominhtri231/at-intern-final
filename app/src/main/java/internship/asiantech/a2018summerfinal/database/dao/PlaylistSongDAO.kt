package internship.asiantech.a2018summerfinal.database.dao

import android.arch.persistence.room.*
import internship.asiantech.a2018summerfinal.database.model.PlaylistSong
import internship.asiantech.a2018summerfinal.database.model.Song

@Dao
interface PlaylistSongDAO {

    @Query(" Select * from song where id in (select song_id from playlist_song where playlist_name = :playlistName)")
    fun getSongOfPlaylist(playlistName: String): List<Song>

    @Delete()
    fun deleteSongInPlaylist(playlistSong: PlaylistSong)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertSongToPlaylist(playlistSong: PlaylistSong)
}
