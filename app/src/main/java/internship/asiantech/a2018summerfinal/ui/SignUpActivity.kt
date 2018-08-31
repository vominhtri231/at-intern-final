package internship.asiantech.a2018summerfinal.ui

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import internship.asiantech.a2018summerfinal.R
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var repeatPassword: String
    private var age: Int = 0
    private lateinit var location: Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val sydney = LatLng(16.0544, 108.2022)
        map.addMarker(MarkerOptions().position(sydney)?.title("Da Nang"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng?) {
        var position = LatLng(0.0, 0.0)
        p0?.let {
            position = LatLng(it.latitude, it.longitude)
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(position))
        btnSignUp.setOnClickListener {
            Log.d("xxxx", "${p0?.latitude} ${p0?.longitude}")
        }
    }
}
