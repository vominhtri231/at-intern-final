package internship.asiantech.a2018summerfinal

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import internship.asiantech.a2018summerfinal.database.AppDataHelper
import internship.asiantech.a2018summerfinal.database.PlaylistUpdater
import internship.asiantech.a2018summerfinal.database.SongUpdater
import internship.asiantech.a2018summerfinal.database.model.Playlist
import internship.asiantech.a2018summerfinal.database.model.Song
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private val dataHelper: AppDataHelper =
            AppDataHelper.getInstance(InstrumentationRegistry.getTargetContext())

    @Test
    fun insertSong() {
        val song1 = Song(name = "a", id = "1", author = "aa")
        val song2 = Song(name = "b", id = "2", author = "bb")
        val song3 = Song(name = "c", id = "3", author = "cc")
        dataHelper.addSong(song1)
        dataHelper.addSong(song2)
        dataHelper.addSong(song3)
        dataHelper.getAllSong(object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.size == 3)
            }
        })
    }

    @Test
    fun deleteSong() {
        val song1 = Song(name = "a", id = "1", author = "aa")
        val song2 = Song(name = "b", id = "2", author = "bb")
        val song3 = Song(name = "c", id = "3", author = "cc")
        dataHelper.addSong(song1)
        dataHelper.addSong(song2)
        dataHelper.addSong(song3)
        dataHelper.deleteSongWithId("1")
        dataHelper.getAllSong(object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.size == 2)
                assert(result[0].id == "2")
            }

        })
    }

    @Test
    fun addPlaylist() {
        val playlist1 = Playlist(name = "p1")
        val playlist2 = Playlist(name = "p2")
        dataHelper.addPlaylist(playlist1)
        dataHelper.addPlaylist(playlist2)
        dataHelper.getAllPlaylist(object : PlaylistUpdater {
            override fun getPlaylistResult(result: List<Playlist>) {
                assert(result.size == 2)
                assert(result[0].name == "p1")
            }
        })
    }

    @Test
    fun deletePlaylist() {
        val playlist1 = Playlist(name = "p1")
        val playlist2 = Playlist(name = "p2")
        dataHelper.addPlaylist(playlist1)
        dataHelper.addPlaylist(playlist2)
        dataHelper.deletePlaylist("p1")
        dataHelper.getAllPlaylist(object : PlaylistUpdater {
            override fun getPlaylistResult(result: List<Playlist>) {
                assert(result.size == 2)
                assert(result[0].name == "p2")
            }
        })
    }

    @Test
    fun addSongToPlaylist() {
        val song1 = Song(name = "a", id = "1", author = "aa")
        val song2 = Song(name = "b", id = "2", author = "bb")
        val song3 = Song(name = "c", id = "3", author = "cc")
        dataHelper.addSong(song1)
        dataHelper.addSong(song2)
        dataHelper.addSong(song3)
        val playlist1 = Playlist(name = "p1")
        val playlist2 = Playlist(name = "p2")
        dataHelper.addPlaylist(playlist1)
        dataHelper.addPlaylist(playlist2)
        dataHelper.addSongToPlaylist("p1", "2")
        dataHelper.addSongToPlaylist("p1", "3")
        dataHelper.getSongInPlaylist("p1", object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.size == 2)
                assert(result[0].id == "2")
            }
        })
        dataHelper.getSongInPlaylist("p2", object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.isEmpty())
            }
        })
    }

    @Test
    fun deleteSongInPlaylist() {
        val song1 = Song(name = "a", id = "1", author = "aa")
        val song2 = Song(name = "b", id = "2", author = "bb")
        val song3 = Song(name = "c", id = "3", author = "cc")
        dataHelper.addSong(song1)
        dataHelper.addSong(song2)
        dataHelper.addSong(song3)
        val playlist1 = Playlist(name = "p1")
        val playlist2 = Playlist(name = "p2")
        dataHelper.addPlaylist(playlist1)
        dataHelper.addPlaylist(playlist2)
        dataHelper.addSongToPlaylist("p1", "2")
        dataHelper.addSongToPlaylist("p1", "3")
        dataHelper.deleteSongInPlaylist("p1", "2")
        dataHelper.getSongInPlaylist("p1", object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.size == 1)
                assert(result[0].id == "3")
            }
        })
    }

    @Test
    fun deletePlaylistAfterAdd() {
        val song1 = Song(name = "a", id = "1", author = "aa")
        val song2 = Song(name = "b", id = "2", author = "bb")
        val song3 = Song(name = "c", id = "3", author = "cc")
        dataHelper.addSong(song1)
        dataHelper.addSong(song2)
        dataHelper.addSong(song3)
        val playlist1 = Playlist(name = "p1")
        val playlist2 = Playlist(name = "p2")
        dataHelper.addPlaylist(playlist1)
        dataHelper.addPlaylist(playlist2)
        dataHelper.addSongToPlaylist("p1", "2")
        dataHelper.addSongToPlaylist("p1", "3")
        dataHelper.deletePlaylist("p1")
        dataHelper.getSongInPlaylist("p1", object : SongUpdater {
            override fun getSongResult(result: List<Song>) {
                assert(result.isEmpty())
            }
        })
    }
}
