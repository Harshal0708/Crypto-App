package com.example.cryptoapp.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.cryptoapp.R

class InternetConnectionActivity : AppCompatActivity() {

    lateinit var txt_connected : TextView
    lateinit var txt_connection : TextView
    lateinit var login_img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet_connection)

        InIt()


    }

    private fun InIt() {

        txt_connected = findViewById(R.id.txt_connected)
        txt_connection = findViewById(R.id.txt_connection)
        login_img = findViewById(R.id.login_img)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->

            if(isConnected){
                login_img.setImageResource(R.drawable.ic_splash)
                txt_connected.text=getString(R.string.connected)
                txt_connected.setTextColor(resources.getColor(R.color.light_green))
                txt_connection.text=getString(R.string.available_connection)

            }else{
                login_img.setImageResource(R.drawable.ic_no_connection)
                txt_connected.text=getString(R.string.disconnected)
                txt_connected.setTextColor(resources.getColor(R.color.red))
                txt_connection.text=getString(R.string.not_available_connection)
            }

        })
    }
}