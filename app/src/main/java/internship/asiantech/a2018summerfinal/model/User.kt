package internship.asiantech.a2018summerfinal.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

class User(val phone: String, val name: String, val password: String, val age: Int, val avatar: Bitmap, val location: LatLng)
