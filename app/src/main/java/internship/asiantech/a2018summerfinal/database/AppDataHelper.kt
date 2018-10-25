package internship.asiantech.a2018summerfinal.database

import android.content.Context
import android.os.AsyncTask
import internship.asiantech.a2018summerfinal.database.dao.PlaylistDAO
import internship.asiantech.a2018summerfinal.database.dao.PlaylistSongDAO
import internship.asiantech.a2018summerfinal.database.dao.SongDAO
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.PlaylistSong
import internship.asiantech.a2018summerfinal.database.model.Song
import internship.asiantech.a2018summerfinal.database.updater.CommonUpdater
import internship.asiantech.a2018summerfinal.database.updater.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.updater.SongUpdater

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
    fun addSong(song: Song): AsyncTask<Song, Unit, Unit> =
            AddSongAsyncTask(database.songDAO()).execute(song)

    /**
     * get all songs in the database
     *
     * @param updater the interface that handle the list of songs
     */
    fun getAllSong(updater: SongUpdater): AsyncTask<Unit, Unit, List<Song>> =
            GetAllSongAsyncTask(database.songDAO(), updater).execute()


    /**
     * delete the song with the id
     *
     * @param id the song's id
     */
    fun deleteSongWithId(id: Long): AsyncTask<Long, Unit, Unit> =
            DeleteSongAsyncTask(database.songDAO()).execute(id)

    /**
     * add playlist to the database
     *
     * @param playlist the playlist that need to add
     */
    fun addPlaylist(playlist: Playlist, updater: CommonUpdater? = null)
            : AsyncTask<Playlist, Unit, Unit> =
            AddPlaylistAsyncTask(database.playlistDAO(), updater).execute(playlist)

    /**
     * get all playlists in the database
     *
     * @param updater the interface that handle the list of playlists
     */
    fun getAllPlaylist(updater: PlaylistUpdater): AsyncTask<Unit, Unit, List<Playlist>> =
            GetAllPlaylistAsyncTask(database.playlistDAO(), updater).execute()

    /**
     * delete playlist with title
     *
     * @param name the playlist's title
     */
    fun deletePlaylist(name: String): AsyncTask<String, Unit, Unit> =
            DeletePlaylistAsyncTask(database.playlistDAO()).execute(name)

    /**
     * insert a song to a playlist
     *
     * @param playlistName the playlist's title
     * @param songId the song's id
     */
    fun addSongToPlaylist(playlistName: String, songId: Long): AsyncTask<PlaylistSong, Unit, Unit> =
            InsertSongToPlaylistAsyncTask(database.playlistSongDAO())
                    .execute(PlaylistSong(playlistName, songId))

    /**
     * get all songs in the playlist
     *
     * @param name the playlist's title
     */
    fun getSongInPlaylist(name: String, updater: SongUpdater): AsyncTask<String, Unit, List<Song>> =
            GetSongInPlaylist(database.playlistSongDAO(), updater).execute(name)

    /**
     * delete a song in a playlist
     *
     * @param playlistName the playlist's title
     * @param songId the song's id
     */
    fun deleteSongInPlaylist(playlistName: String, songId: Long)
            : AsyncTask<PlaylistSong, Unit, Unit> =
            DeleteSongInPlaylistAsyncTask(database.playlistSongDAO())
                    .execute(PlaylistSong(playlistName, songId))

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

private class DeleteSongAsyncTask(private val songDAO: SongDAO) : AsyncTask<Long, Unit, Unit>() {
    override fun doInBackground(vararg p0: Long?) {
        p0[0]?.let {
            songDAO.deleteSongWithId(it)
        }

    }
}

private class AddPlaylistAsyncTask(private val playlistDAO: PlaylistDAO,
                                   private val updater: CommonUpdater?)
    : AsyncTask<Playlist, Unit, Unit>() {
    override fun doInBackground(vararg p: Playlist?) {
        p[0]?.let { playlistDAO.addPlaylist(it) }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        updater?.onFinish()
    }
}

private class GetAllPlaylistAsyncTask(private val playlistDAO: PlaylistDAO,
                                      private val updater: PlaylistUpdater)
    : AsyncTask<Unit, Unit, List<Playlist>>() {
    override fun doInBackground(vararg p: Unit): List<Playlist> {
        return playlistDAO.getAllPlaylist()
    }

    override fun onPostExecute(result: List<Playlist>) {
        super.onPostExecute(result)
        updater.getPlaylistResult(result)
    }
}

private class DeletePlaylistAsyncTask(private val playlistDAO: PlaylistDAO)
    : AsyncTask<String, Unit, Unit>() {
    override fun doInBackground(vararg p: String) {
        playlistDAO.deletePlaylistWithName(p[0])
    }
}

private class GetSongInPlaylist(private val playlistSongDAO: PlaylistSongDAO,
                                private val updater: SongUpdater)
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
