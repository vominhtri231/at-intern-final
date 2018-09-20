package internship.asiantech.a2018summerfinal.utils

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import internship.asiantech.a2018summerfinal.database.model.Song

/**
 * query all songs in the device
 *
 * @param contentResolver
 * @return list of song
 */
fun querySongs(contentResolver: ContentResolver): List<Song> {
    val cursor: Cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null)

    val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
    val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
    val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

    val result = mutableListOf<Song>()
    cursor.moveToFirst()
    do {
        val song = Song(
                id = cursor.getLong(idColumn),
                title = cursor.getString(titleColumn),
                artist = cursor.getString(artistColumn))
        result.add(song)
    } while (cursor.moveToNext())

    cursor.close()
    return result
}
