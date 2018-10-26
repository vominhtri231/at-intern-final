package internship.asiantech.a2018summerfinal.utils

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import internship.asiantech.a2018summerfinal.database.model.Song


fun searchSong(songs: List<Song>, keySearch: String): List<Song> {
    val result = mutableListOf<Song>()
    for (song in songs) {
        if (isWordInString(song.title, keySearch) || isWordInString(song.artist, keySearch)) {
            result.add(song)
        }
    }
    return result
}

fun querySongs(contentResolver: ContentResolver): List<Song> {
    val musicCursor = getCursorQuery(contentResolver)
    val result = fetchDataFromCursor(musicCursor)
    musicCursor.close()
    return result
}

private fun getCursorQuery(contentResolver: ContentResolver): Cursor {
    val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
    val project = arrayOf(MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM)
    return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            project, selection, null, null)
}

private fun fetchDataFromCursor(cursor: Cursor): List<Song> {
    val result = mutableListOf<Song>()
    if (cursor.moveToFirst()) {
        val titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
        val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
        val artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST)
        val albumColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM)
        do {
            val song = Song(
                    id = cursor.getLong(idColumn),
                    title = cursor.getString(titleColumn),
                    artist = cursor.getString(artistColumn),
                    album = cursor.getString(albumColumn))
            result.add(song)
        } while (cursor.moveToNext())
    }
    return result
}
