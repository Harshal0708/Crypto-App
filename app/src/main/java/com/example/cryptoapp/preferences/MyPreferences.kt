package com.example.cryptoapp.preferences

import android.content.Context
import com.example.cryptoapp.Response.LoginUserDataResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.util.Objects

class MyPreferences(context: Context) {
    val PREFERENCES_NAME = "SharedPreferencesCrypto"
    val LOGIN_USER_DETAIL = "LoginUserDetail"
    val REMEMBER_ME = "RememberMe"

    val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getLogin(): String {
        val login = preferences.getString(LOGIN_USER_DETAIL, "")
        return login.toString()
    }

    fun getRemember(): Boolean {
        val remember = preferences.getBoolean(REMEMBER_ME, false)
        return remember
    }

    fun setLogin(data: LoginUserDataResponse?) {
        val editor = preferences.edit()
        editor.putString(LOGIN_USER_DETAIL, Gson().toJson(data))
        editor.apply()
        editor.commit()
    }

    fun setRemember(isChecked: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(REMEMBER_ME, isChecked)
        editor.apply()
        editor.commit()
    }


}