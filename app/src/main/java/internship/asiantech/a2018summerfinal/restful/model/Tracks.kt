package internship.asiantech.a2018summerfinal.restful.model

import com.google.gson.annotations.SerializedName

class Tracks {
    var track: List<Track> = listOf()
    @SerializedName("@attr")
    var attr: Attr = Attr()
}
