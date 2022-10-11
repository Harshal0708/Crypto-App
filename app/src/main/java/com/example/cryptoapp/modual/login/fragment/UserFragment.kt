package com.example.cryptoapp.modual.login.fragment

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.cryptoapp.R
import java.util.regex.Pattern

class UserFragment : Fragment(), View.OnClickListener {

    var user_btn_email: Button? = null
    var user_btn_password: Button? = null
    var create_account: Button? = null
    var sp_et_email: EditText? = null
    var mn_et_phone: EditText? = null
    var sp_et_firstName: EditText? = null
    var sp_et_lastName: EditText? = null
    var sp_et_password: EditText? = null
    var sp_et_rePassword: EditText? = null
    var isUser: Boolean = false
    private lateinit var email: String

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    val PHONE_NUMBER_PATTERN = Pattern.compile(
        "[0-9]{10}"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_user, container, false)

        init(view)
        return view
    }

    fun init(view: View) {
        user_btn_email = view?.findViewById(R.id.user_btn_email)
        user_btn_password = view?.findViewById(R.id.user_btn_password)
        create_account = view?.findViewById(R.id.create_account)
        sp_et_firstName = view?.findViewById(R.id.sp_et_firstName)
        sp_et_lastName = view?.findViewById(R.id.sp_et_lastName)
        sp_et_password = view?.findViewById(R.id.sp_et_password)
        sp_et_password = view?.findViewById(R.id.sp_et_password)
        sp_et_rePassword = view?.findViewById(R.id.sp_et_rePassword)

        sp_et_email = view?.findViewById(R.id.sp_et_email)
        mn_et_phone = view?.findViewById(R.id.mn_et_phone)

        user_btn_password!!.setBackgroundColor(0)
        sp_et_email?.visibility = View.VISIBLE
        mn_et_phone?.visibility = View.GONE
        isUser = false

        user_btn_email!!.setOnClickListener(this)
        user_btn_password!!.setOnClickListener(this)
        create_account!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.user_btn_email -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity?.let { it1 -> user_btn_email!!.setBackgroundColor(it1.getColor(R.color.light_grey)) }
                }
                user_btn_password!!.setBackgroundColor(0)
                mn_et_phone?.visibility = View.GONE
                sp_et_email?.visibility = View.VISIBLE
                isUser = false
            }
            R.id.user_btn_password -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity?.getColor(R.color.light_grey)
                        ?.let { it1 -> user_btn_password!!.setBackgroundColor(it1) }
                }
                user_btn_email!!.setBackgroundColor(0)
                mn_et_phone?.visibility = View.VISIBLE
                sp_et_email?.visibility = View.GONE
                isUser = true
            }

            R.id.create_account -> {
                signUp(isUser)
            }
        }
    }


    fun signUp(isUser: Boolean): Boolean {

        if (sp_et_firstName?.length() == 0) {
            sp_et_firstName?.setError(getString(R.string.valid_error));
            return false;
        }

        if (sp_et_lastName?.length() == 0) {
            sp_et_lastName?.setError(getString(R.string.valid_error));
            return false;
        }

        if (isUser == false) {

            if (sp_et_email?.length() == 0) {
                sp_et_email?.setError(getString(R.string.valid_error));
                return false;
            }else{

                val str_email = sp_et_email?.text.toString().trim()
                if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(str_email))) {
                    sp_et_email?.setError(getString(R.string.email_error));
                    return false;
                }
            }


        } else {

            if (mn_et_phone?.length() == 0) {
                mn_et_phone?.setError(getString(R.string.valid_error));
                return false;
            }

            val str_number = mn_et_phone?.text.toString().trim()
            if (!(PHONE_NUMBER_PATTERN.toRegex().matches(str_number))) {
                mn_et_phone?.setError(getString(R.string.phone_error));
                return false;
            }
        }

        if (sp_et_password?.length() == 0) {
            sp_et_password?.setError(getString(R.string.password_error));
            return false;
        }

        if (sp_et_rePassword?.length() == 0) {
            sp_et_rePassword?.setError(getString(R.string.repassword_error));
            return false;
        }



        if (isUser == false) {
            Toast.makeText(activity, "Email", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(activity, "Mobile Number", Toast.LENGTH_SHORT).show()
            return true
        }


    }
}