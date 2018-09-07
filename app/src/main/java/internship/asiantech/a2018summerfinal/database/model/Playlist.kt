package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "playlist")
data class Playlist(
        @PrimaryKey
        @NotNull
        @ColumnInfo(name = "name")
        var name: String = ""
)
