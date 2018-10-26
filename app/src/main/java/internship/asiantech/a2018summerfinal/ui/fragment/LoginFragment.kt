package internship.asiantech.a2018summerfinal.ui.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.ui.fragment.listener.LoginFragmentListener
import internship.asiantech.a2018summerfinal.utils.checkUserLogin
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var listener: LoginFragmentListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement LoginFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getAttackMail()
    }

    private fun getAttackMail(){
        arguments?.getString(MAIL_KEY).let{
            edtMail.setText(it)
        }
    }

    private fun initListeners() {
        tvSignUp.setOnClickListener {
            listener.onStartSignUp()
        }

        btnLogin.setOnClickListener {
            val mail = edtMail.text.toString()
            val password = edtPassword.text.toString()
            login(mail, password)
        }
    }

    private fun login(mail: String, password: String) {
        val result = checkUserLogin(mail, password)

        if (!result.isSuccess()) {
            displayErrorMessage(result.getMessage())
        } else {
            listener.onLogin(mail, password)
        }
    }

    fun displayErrorMessage(messageReference: Int) {
        tvError.text = resources.getString(messageReference)
        tvError.setBackgroundResource(R.drawable.border_text_view_error)
    }

    companion object {
        const val MAIL_KEY = "SIGN_UP_MAIL_KEY"

        fun newInstance(mail:String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        this.putString(MAIL_KEY, mail)
                    }
                }
    }
}
