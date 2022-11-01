package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import java.util.regex.Pattern

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var edFirstname: EditText
    lateinit var edLastname: EditText
    lateinit var edEmail: EditText
    lateinit var edPhone: EditText
    lateinit var view: View
    lateinit var register_progressBar: ProgressBar
    lateinit var resent: TextView
    lateinit var progressBar_cardView: RelativeLayout

    private lateinit var email: String
    private lateinit var phone: String

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }

    private fun init() {
        edFirstname = findViewById(R.id.edFirstname)
        edLastname = findViewById(R.id.edLastname)
        edEmail = findViewById(R.id.edEmail)
        edPhone = findViewById(R.id.edPhone)
        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)

        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility=View.VISIBLE
        resent = view.findViewById(R.id.resent)
        resent.text = "Save"
        progressBar_cardView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.progressBar_cardView -> {

                if (validation() == true) {
                    save()
                }
            }

        }
    }

    private fun save() {
        register_progressBar.visibility=View.GONE
        Toast.makeText(this@ProfileActivity, "Save", Toast.LENGTH_LONG)
            .show()
    }


    fun validation(): Boolean {

        if (edFirstname?.length() == 0) {
            edFirstname?.setError(getString(R.string.valid_error));
            return false;
        }

        if (edLastname?.length() == 0) {
            edLastname?.setError(getString(R.string.valid_error));
            return false;
        }

        if (edEmail?.length() == 0) {
            edEmail?.setError(getString(R.string.valid_error));
            return false;
        }

        email = edEmail?.text.toString().trim()
        if (!(EMAIL_ADDRESS_PATTERN.toRegex().matches(email))) {
            edEmail?.setError(getString(R.string.email_error));
            return false;
        }

        if (edPhone?.length() == 0) {
            edPhone?.setError(getString(R.string.valid_error));
            return false;
        }

        phone = edPhone?.text.toString().trim()
        if (!(PHONE_NUMBER_PATTERN.toRegex().matches(phone))) {
            edPhone?.setError(getString(R.string.phone_error));
            return false;
        }

        return true
    }

}