package internship.asiantech.a2018summerfinal.database

import android.content.Context
import android.os.AsyncTask
import internship.asiantech.a2018summerfinal.database.dao.PlaylistDAO
import internship.asiantech.a2018summerfinal.database.dao.PlaylistSongDAO
import internship.asiantech.a2018summerfinal.database.dao.SongDAO
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.PlaylistSong
import internship.asiantech.a2018summerfinal.database.model.Song

/**
 * RTFI
 */
class AppDataHelper private constructor(val database: AppDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: AppDataHelper? = null

        /**
         * To get an AppDataHelper:
         * AppDataHelper.getInstance(getContext)
         *
         * @param context the context where we need AppDataHelper
         */
        fun getInstance(context: Context): AppDataHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: AppDataHelper(AppDatabase.getInstance(context))
                            .also { INSTANCE = it }
                }
    }

    /**
     * insert song into database
     *
     * @param song the song that need to add
     */
    fun addSong(song: Song) = AddSongAsyncTask(database.songDAO()).execute(song)

    /**
     * get all songs in the database
     *
     * @param updater the interface that handle the list of songs
     */
    fun getAllSong(updater: SongUpdater) = GetAllSongAsyncTask(database.songDAO(), updater).execute()


    /**
     * delete the song with the id
     *
     * @param id the song's id
     */
    fun deleteSongWithId(id: String) = DeleteSongAsyncTask(database.songDAO()).execute()

    /**
     * add playlist to the database
     *
     * @param playlist the playlist that need to add
     */
    fun addPlaylist(playlist: Playlist) = AddPlaylistAsyncTask(database.playlistDAO()).execute()

    /**
     * get all playlists in the database
     *
     * @param updater the interface that handle the list of playlists
     */
    fun getAllPlaylist(updater: PlaylistUpdater) = GetAllPlaylistAsyncTask(database.playlistDAO(), updater).execute()

    /**
     * delete playlist with name
     *
     * @param name the playlist's name
     */
    fun deletePlaylist(name: String) = DeletePlaylistAsyncTask(database.playlistDAO()).execute(name)

    /**
     * insert a song to a playlist
     *
     * @param playlistName the playlist's name
     * @param songId the song's id
     */
    fun addSongToPlaylist(playlistName: String, songId: String) =
            InsertSongToPlaylistAsyncTask(database.playlistSongDAO()).execute(PlaylistSong(playlistName, songId))

    /**
     * get all songs in the playlist
     *
     * @param name the playlist's name
     */
    fun getSongInPlaylist(name: String, updater: SongUpdater) =
            GetSongInPlaylist(database.playlistSongDAO(), updater).execute(name)

    /**
     * delete a song in a playlist
     *
     * @param playlistName the playlist's name
     * @param songId the song's id
     */
    fun deleteSongInPlaylist(playlistName: String, songId: String) =
            DeleteSongInPlaylistAsyncTask(database.playlistSongDAO()).execute(PlaylistSong(playlistName, songId))

}

interface SongUpdater {
    fun getSongResult(result: List<Song>)
}

interface PlaylistUpdater {
    fun getPlaylistResult(result: List<Playlist>)
}

private class AddSongAsyncTask(private val songDAO: SongDAO) : AsyncTask<Song, Unit, Unit>() {
    override fun doInBackground(vararg p: Song?) {
        p[0]?.let { songDAO.addSong(it) }
    }
}

private class GetAllSongAsyncTask(private val songDAO: SongDAO, private val updater: SongUpdater)
    : AsyncTask<Unit, Unit, List<Song>>() {
    override fun doInBackground(vararg p: Unit): List<Song> {
        return songDAO.getAllSong()
    }

    override fun onPostExecute(result: List<Song>) {
        super.onPostExecute(result)
        updater.getSongResult(result)
    }
}

private class DeleteSongAsyncTask(private val songDAO: SongDAO) : AsyncTask<String, Unit, Unit>() {
    override fun doInBackground(vararg p: String) {
        songDAO.deleteSongWithId(p[0])
    }
}

private class AddPlaylistAsyncTask(private val playlistDAO: PlaylistDAO) : AsyncTask<Playlist, Unit, Unit>() {
    override fun doInBackground(vararg p: Playlist?) {
        p[0]?.let { playlistDAO.addPlaylist(it) }
    }
}

private class GetAllPlaylistAsyncTask(private val playlistDAO: PlaylistDAO, private val updater: PlaylistUpdater)
    : AsyncTask<Unit, Unit, List<Playlist>>() {
    override fun doInBackground(vararg p: Unit): List<Playlist> {
        return playlistDAO.getAllPlaylist()
    }

    override fun onPostExecute(result: List<Playlist>) {
        super.onPostExecute(result)
        updater.getPlaylistResult(result)
    }
}

private class DeletePlaylistAsyncTask(private val playlistDAO: PlaylistDAO) : AsyncTask<String, Unit, Unit>() {
    override fun doInBackground(vararg p: String) {
        playlistDAO.deletePlaylistWithName(p[0])
    }
}

private class GetSongInPlaylist(private val playlistSongDAO: PlaylistSongDAO, private val updater: SongUpdater)
    : AsyncTask<String, Unit, List<Song>>() {
    override fun doInBackground(vararg p: String): List<Song> {
        return playlistSongDAO.getSongOfPlaylist(p[0])
    }

    override fun onPostExecute(result: List<Song>) {
        super.onPostExecute(result)
        updater.getSongResult(result)
    }
}

private class DeleteSongInPlaylistAsyncTask(private val playlistSongDAO: PlaylistSongDAO)
    : AsyncTask<PlaylistSong, Unit, Unit>() {
    override fun doInBackground(vararg p: PlaylistSong) {
        playlistSongDAO.deleteSongInPlaylist(p[0])
    }
}

private class InsertSongToPlaylistAsyncTask(private val playlistSongDAO: PlaylistSongDAO)
    : AsyncTask<PlaylistSong, Unit, Unit>() {
    override fun doInBackground(vararg p: PlaylistSong?) {
        p[0]?.let { playlistSongDAO.insertSongToPlaylist(it) }
    }
}
