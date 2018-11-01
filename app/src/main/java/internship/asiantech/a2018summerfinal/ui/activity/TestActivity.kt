package internship.asiantech.a2018summerfinal.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.restful.MusicAPI
import internship.asiantech.a2018summerfinal.restful.model.TopTrackResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;

class TestActivity : AppCompatActivity() {
    private val callback: Callback<TopTrackResult> = object : Callback<TopTrackResult> {
        override fun onFailure(call: Call<TopTrackResult>, t: Throwable) {
            Log.e("TAG", t.message)
        }

        override fun onResponse(call: Call<TopTrackResult>, response: Response<TopTrackResult>) {
            Log.e("TAG", response.message())
            if (response.isSuccessful) {
                Log.e("TAG", response.body()?.tracks?.track?.size.toString())
            }
        }
    }
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val musicRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(MusicAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    private val api = musicRetrofit.create(MusicAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        api.getTopTrack(MusicAPI.TOKEN).enqueue(callback)
    }
}
