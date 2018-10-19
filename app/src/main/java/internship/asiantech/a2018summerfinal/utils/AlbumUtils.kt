package internship.asiantech.a2018summerfinal.utils

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import internship.asiantech.a2018summerfinal.model.Album

fun queryAlbum(contentResolver: ContentResolver): List<Album> {
    val musicCursor = getCursorQuery(contentResolver)
    val result = fetchDataFromCursor(musicCursor)
    musicCursor.close()
    return result
}

private fun getCursorQuery(contentResolver: ContentResolver): Cursor {
    val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS)
    val selection: String? = null
    val selectionArgs: Array<String>? = null
    val sortOrder = MediaStore.Audio.Media.ALBUM + " ASC"
    return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder)
}

private fun fetchDataFromCursor(cursor: Cursor): List<Album> {
    val result = mutableListOf<Album>()
    if (cursor.moveToFirst()) {
        val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums._ID)
        val albumColmn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM)
        val artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ARTIST)
        val numberOfSongsColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.NUMBER_OF_SONGS)
        do {
            val album = Album(
                    id = cursor.getLong(idColumn),
                    name = cursor.getString(albumColmn),
                    artist = cursor.getString(artistColumn),
                    numberOfSongs = cursor.getInt(numberOfSongsColumn))
            result.add(album)
        } while (cursor.moveToNext())
    }
    return result
}
