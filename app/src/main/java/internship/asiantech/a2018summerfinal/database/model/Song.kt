package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "song")
data class Song(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "author")
        var author: String = "",

        @ColumnInfo(name = "duration")
        var duration: Int = 0,

        @ColumnInfo(name = "isFavourite")
        var isFavourite: Boolean = false
)
