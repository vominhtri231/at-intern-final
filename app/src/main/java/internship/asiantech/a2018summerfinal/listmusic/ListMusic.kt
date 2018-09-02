package internship.asiantech.a2018summerfinal.listmusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import internship.asiantech.a2018summerfinal.model.Music

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

                val musicResolver = context.contentResolver
                val musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val musicCursor = musicResolver.query(musicUri, null, null, null, null)
                if (musicCursor != null && musicCursor.moveToFirst()) {
                    val titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
                    val idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
                    val artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST)
                    do {
                        val thisId = musicCursor.getLong(idColumn)
                        val thisTitle = musicCursor.getString(titleColumn)
                        val thisArtist = musicCursor.getString(artistColumn)
                        listMusics?.add(Music(thisId, thisTitle, thisArtist))
                    } while (musicCursor.moveToNext())
                }
            }
        }
        return listMusics as ArrayList<Music>
    }
}
