package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "playlist_song",
        foreignKeys = [
            ForeignKey(
                    entity = Playlist::class,
                    parentColumns = arrayOf("name"),
                    childColumns = arrayOf("playlist_name"),
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE),
            ForeignKey(
                    entity = Song::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("song_id"),
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE)],
        primaryKeys = ["playlist_name", "song_id"])

data class PlaylistSong(
        @ColumnInfo(name = "playlist_name")
        var playlistName: String = "",

        @ColumnInfo(name = "song_id")
        var songId: String = ""
)
