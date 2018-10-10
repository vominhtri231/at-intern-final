package internship.asiantech.a2018summerfinal.ui.dialog

import internship.asiantech.a2018summerfinal.database.model.Song

interface EventAddPlaylistListener {
    fun addPlaylist(name : String,listSongs : MutableList<Song>)
}
