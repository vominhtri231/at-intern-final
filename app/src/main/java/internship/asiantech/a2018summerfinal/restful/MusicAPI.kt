package internship.asiantech.a2018summerfinal.restful

import internship.asiantech.a2018summerfinal.restful.model.TopTrackResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicAPI {
    companion object {
        const val TOKEN: String = "18c520c0e3bb693d74a9831c123dd7c8"
        const val BASE_URL: String = "http://ws.audioscrobbler.com/2.0/"
    }

    @GET("?method=chart.gettoptracks&format=json")
    fun getTopTrack(@Query("api_key") token: String): Call<TopTrackResult>
}
