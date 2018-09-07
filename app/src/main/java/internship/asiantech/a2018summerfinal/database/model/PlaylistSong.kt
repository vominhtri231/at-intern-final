package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "playlist_song",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Playlist::class,
                        parentColumns = arrayOf("name"),
                        childColumns = arrayOf("playlist_name"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(
                        entity = Song::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("song_id"),
                        onDelete = ForeignKey.CASCADE)))

data class PlaylistSong(
        @PrimaryKey
        @NotNull
        @ColumnInfo(name = "playlist_name")
        var playlistName: String = "",

        @PrimaryKey
        @NotNull
        @ColumnInfo(name = "song_id")
        var songId: String = ""
)
