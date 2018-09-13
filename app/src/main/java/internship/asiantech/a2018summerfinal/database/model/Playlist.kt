package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String = ""
)
