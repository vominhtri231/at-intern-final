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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeInt(duration)
        parcel.writeByte(if (isFavourite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}
