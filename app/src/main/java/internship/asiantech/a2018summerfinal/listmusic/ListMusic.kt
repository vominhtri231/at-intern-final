package internship.asiantech.a2018summerfinal.listmusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import internship.asiantech.a2018summerfinal.model.Music
import android.provider.MediaStore


class ListMusic(private val context: Context) {
    private var listMusics: ArrayList<Music>? = null

    companion object {
        private const val REQUEST_PERMISSION = 1
    }

    @SuppressLint("Recycle")
    fun getListMusics(): ArrayList<Music> {
        if (listMusics == null) {
            listMusics = ArrayList()
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            } else {
                val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
                val project = arrayOf(MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM)
                val musicCursor: Cursor
                musicCursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project, selection, null, null)
                if (musicCursor != null && musicCursor.moveToFirst()) {
                    val titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
                    val idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
                    val artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST)
                    val duration = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION)
                    do {
                        val thisId = musicCursor.getLong(idColumn)
                        val thisTitle = musicCursor.getString(titleColumn)
                        val thisArtist = musicCursor.getString(artistColumn)
                        listMusics?.add(Music(thisId, thisTitle, thisArtist, duration))
                    } while (musicCursor.moveToNext())
                }
                musicCursor.close()
            }
        }
        return listMusics as ArrayList<Music>
    }
}
