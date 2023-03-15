package com.example.cryptoapp.modual.login.fragment

import android.content.Intent
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
import com.airbnb.lottie.LottieAnimationView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.CmsAdsAddResponse
import com.example.cryptoapp.Response.DataX
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.model.CreateApiKeysPayload
import com.example.cryptoapp.modual.login.ForgotPasswordActivity
import com.example.cryptoapp.modual.login.UserActivity
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import com.strings.cryptoapp.Response.GenerateQrCodeResponnse
import com.strings.cryptoapp.model.CreateUserGAKeyPayload
import java.util.regex.Pattern

class UserFragment : Fragment(), View.OnClickListener {

    lateinit var doc_submit : Button
    lateinit var doc_api_key : EditText
    lateinit var doc_secret : EditText
    lateinit var preferences: MyPreferences
    lateinit var userDetail: DataXX

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_user, container, false)

        Init(view)
        return view
    }

    private fun Init(view: View) {
        preferences = MyPreferences(requireContext())
        userDetail = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        doc_submit = view.findViewById(R.id.doc_submit)
        doc_api_key = view.findViewById(R.id.doc_api_key)
        doc_secret = view.findViewById(R.id.doc_secret)

        doc_submit.setOnClickListener(this)

    }

    private fun addCreateApiKeys() {
        //register_progressBar.visibility = View.VISIBLE

        val response =
            context?.let { ServiceBuilder(it).buildService(RestApi::class.java) }

        val payload = CreateApiKeysPayload(
            doc_api_key.text.toString(),
            doc_secret.text.toString(),
            userDetail.userId
        )

        response!!.addCreateApiKeys(payload)
            .enqueue(object : retrofit2.Callback<CmsAdsAddResponse> {
                override fun onResponse(
                    call: retrofit2.Call<CmsAdsAddResponse>,
                    response: retrofit2.Response<CmsAdsAddResponse>
                ) {

                    if (response.body()?.isSuccess == true) {
                       Constants.showToast(context!!,"Document Verification Successfully.")
                    }

                }
                override fun onFailure(
                    call: retrofit2.Call<CmsAdsAddResponse>,
                    t: Throwable
                ) {

                    //register_progressBar.visibility = View.GONE
                    Constants.showToast(
                        context!!,
                        getString(R.string.login_failed)
                    )
                }
            })
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {

            R.id.doc_submit -> {
                addCreateApiKeys()
            }
        }
    }
}