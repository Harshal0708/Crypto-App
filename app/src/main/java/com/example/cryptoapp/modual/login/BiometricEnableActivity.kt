package com.example.cryptoapp.modual.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.biometric.BiometricManager
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson

class BiometricEnableActivity : AppCompatActivity(), View.OnClickListener {

   private lateinit var info: String
   private lateinit var view: View
   private lateinit var register_progressBar: ProgressBar
   private lateinit var resent: TextView
   private lateinit var progressBar_cardView: RelativeLayout
   private var isEnabled: Boolean = false

    lateinit var preferences: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_enable)

        Init()
    }

    private fun Init() {
        preferences = MyPreferences(this)

        view = findViewById(R.id.btn_progressBar)
        register_progressBar = view.findViewById(R.id.register_progressBar)
        progressBar_cardView = view.findViewById(R.id.progressBar_cardView)
        register_progressBar.visibility = View.GONE
        resent = view.findViewById(R.id.resent)
        resent.text = getString(R.string.enable)
        progressBar_cardView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {

            R.id.progressBar_cardView -> {
                checkDeviceHasBiometric()
            }
        }
    }

    fun checkDeviceHasBiometric() {

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                info = "App can authenticate using biometrics."
                Constants.showLog("BIOMETRIC_SUCCESS: ",info)
                isEnabled = true

//                if (intent.getBooleanExtra("isChecked", false) == true) {
//                    preferences.setRemember(true)
//                } else {
//                    preferences.setRemember(false)
//                }
//
//                preferences.setLogin(data)
//                preferences.setToken(data.accessToken)

                val intent = Intent(this, BiometricActivity::class.java)
                intent.putExtra("isChecked", intent.getBooleanExtra("isChecked", false))
                startActivity(intent)

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                info = "No biometric features available on this device."
                Constants.showLog("BIOMETRIC_ERROR_NO_HARDWARE: ",info)
                isEnabled = false
                Constants.showToast(this@BiometricEnableActivity,this@BiometricEnableActivity,info)

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

                info = "Biometric features are currently unavailable."
                Constants.showLog("BIOMETRIC_ERROR_HW_UNAVAILABLE: ",info)
                isEnabled = false
                Constants.showToast(this@BiometricEnableActivity,this@BiometricEnableActivity,info)

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG or android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                isEnabled = false

                startActivityForResult(enrollIntent, 100)
            }
        }

    }

}