package internship.asiantech.a2018summerfinal.fragment.dialog

import internship.asiantech.a2018summerfinal.database.model.Song

interface EventAddPlaylistListener {
    fun addPlaylist(name : String,listSongs : MutableList<Song>)
}
