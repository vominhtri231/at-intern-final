package internship.asiantech.a2018summerfinal.ui.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.User
import internship.asiantech.a2018summerfinal.ui.fragment.listener.SignUpFragmentListener
import internship.asiantech.a2018summerfinal.utils.checkUserSignUp
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private lateinit var listener: SignUpFragmentListener
    private var location: LatLng = LatLng(16.0544, 108.2022)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SignUpFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement SignUpFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initListeners()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let { map = it }
        val danang = LatLng(16.0544, 108.2022)
        map.moveCamera(CameraUpdateFactory.newLatLng(danang))
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng?) {
        p0?.let {
            location = it
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun initListeners() {
        btnSignUp.setOnClickListener {
            val mail = edtMail.text.toString()
            val name = edtName.text.toString()
            val password = edtPassword.text.toString()
            val repeatPassword = edtRepeatPassword.text.toString()
            val ageString = edtAge.text.toString()

            val newUser = User(mail = mail, password = password, name = name,
                    longitude = location.longitude, latitude = location.latitude)
            signUp(newUser, repeatPassword, ageString)
        }
    }

    private fun signUp(user: User, repeatPassword: String, ageString: String) {
        val result = checkUserSignUp(user, repeatPassword, ageString)
        if (!result.isSuccess()) {
            displayErrorMessage(result.getMessage())
        } else {
            user.age = ageString.toInt()
            createUser(user)
        }
    }

    private fun createUser(user: User) {
        listener.onSignUp(user)
    }

    fun displayErrorMessage(messageReference: Int) {
        tvError.text = resources.getString(messageReference)
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
    }
}
