package com.example.cryptoapp.preferences

import android.content.Context
import com.example.cryptoapp.Response.DataXX
import com.google.gson.Gson

class
MyPreferences(context: Context) {
    val PREFERENCES_NAME = "SharedPreferencesCrypto"
    val LOGIN_USER_DETAIL = "LoginUserDetail"
    val REMEMBER_ME = "RememberMe"
    val BIO_ENABLE = "BioEnable"
    val SELECT_AUTH = "SelectAuth"
    val TOKEN = "Token"

    val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setLogin(data: DataXX?) {
        val editor = preferences.edit()
        editor.putString(LOGIN_USER_DETAIL, Gson().toJson(data))
        editor.apply()
        editor.commit()
    }

    fun getLogin(): String {
        val login = preferences.getString(LOGIN_USER_DETAIL, "")
        return login.toString()
    }

    fun setRemember(isChecked: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(REMEMBER_ME, isChecked)
        editor.apply()
        editor.commit()
    }

    fun getRemember(): Boolean {
        val remember = preferences.getBoolean(REMEMBER_ME, false)
        return remember
    }

    fun setToken(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun getToken(): String {
        val remember = preferences.getString(TOKEN, "")
        return remember.toString()
    }

     fun setAuth(auth: Int) {
        val editor = preferences.edit()
        editor.putInt(SELECT_AUTH, auth)
        editor.apply()
        editor.commit()
    }

    fun getAuth(): Int {
        val remember = preferences.getInt(SELECT_AUTH, 0)
        return remember
    }



    fun setEnable(isChecked: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(BIO_ENABLE, isChecked)
        editor.apply()
        editor.commit()
    }

    fun getEnable(): Boolean {
        val remember = preferences.getBoolean(BIO_ENABLE, false)
        return remember
    }
}