package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.LoginUserDataResponse
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    lateinit var preferences : MyPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view=inflater.inflate(R.layout.fragment_profile, container, false)

        preferences = MyPreferences(requireContext())
        var testModel = Gson().fromJson(preferences.getLogin(), LoginUserDataResponse::class.java)

        Log.d("test", testModel.userId.toString())
        return view
    }

}