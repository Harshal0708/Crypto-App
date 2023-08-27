package com.example.cryptoapp.modual.login

import android.content.Intent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.preferences.MyPreferences
import com.example.cryptoapp.singleton.MySingleton
import com.google.gson.Gson
import java.util.concurrent.Executor


class BiometricActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var info: String

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var imgFingure: ImageView
    private lateinit var txt_button_login: TextView
    private lateinit var img_back: ImageView

    lateinit var preferences: MyPreferences
    lateinit var data: DataXX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)



        preferences = MyPreferences(this)

//        data = Gson().fromJson(intent.getStringExtra("data"), DataXX::class.java)
        data = MySingleton().getData()

        imgFingure = findViewById(R.id.imgFingure)
        txt_button_login = findViewById(R.id.txt_button_login)
        img_back = findViewById(R.id.img_back)

        txt_button_login.setOnClickListener(this)
        img_back.setOnClickListener(this)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error:", Toast.LENGTH_SHORT
                    )
                        .show()
                    Constants.showLog("onAuthenticationError: ", "$errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)

                    preferences.setEnable(true)

                    if (intent.getBooleanExtra("isChecked", false) == true) {
                        preferences.setRemember(true)
                    } else {
                        preferences.setRemember(false)
                    }

                    preferences.setLogin(data)
                    preferences.setToken(data.accessToken)

                    var intent = Intent(this@BiometricActivity, UserActivity::class.java)
                    startActivity(intent)


                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    Constants.showLog("onAuthenticationSucceeded: ", "Authentication succeeded!")
                    Constants.showLog("succeeded result: ", result.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Constants.showLog("onAuthenticationFailed: ", "Authentication failed!")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txt_button_login -> {
                biometricPrompt.authenticate(promptInfo)
            }
            R.id.img_back -> {
                preferences.setEnable(false)
                var intent = Intent(this@BiometricActivity, LoginActivity::class.java)
                startActivity(intent)
                this@BiometricActivity.finish()
            }

        }
    }
}

