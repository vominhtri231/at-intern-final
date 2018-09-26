package internship.asiantech.a2018summerfinal.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "song")
data class Song(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "title")
        var title: String = "",

        @ColumnInfo(name = "artist")
        var artist: String = "",

        @ColumnInfo(name = "duration")
        var duration: Int = 0,

        @ColumnInfo(name = "isFavourite")
        var isFavourite: Boolean = false
) 
