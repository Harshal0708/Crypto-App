package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


class PLFragment : Fragment() {
    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    lateinit var data: DataXX
    lateinit var output: TextView
    lateinit var preferences: MyPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_p_l, container, false)

        output = view.findViewById(R.id.output)
        output.text="WPL"
        preferences = MyPreferences(requireContext())
        client = OkHttpClient()
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        initWebSocket()
        return view
    }
    private fun initWebSocket() {

        webSocketListener = object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                //Constants.showLog("test${"reason"}", reason)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                super.onFailure(webSocket, t, response)
                Constants.showLog("test", t.message.toString())
                Constants.showLog("test${"messagePL"}", t.message.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                setUpBtcPriceText(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Constants.showLog("test${"onMessage2"}", bytes.hex())
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                webSocket.send(data.userId)
            }
        }

        //val request: Request = Request.Builder().url("wss://stream.binance.com:443/ws/!ticker@arr").build()

        val request: Request = Request.Builder().url("ws://103.14.99.61:8084/getPL").build()
        val ws = client!!.newWebSocket(request, webSocketListener)
        client!!.dispatcher.executorService.shutdown()
    }

    fun setUpBtcPriceText(message: String?) {
        message?.let {

//            val gson = Gson()
//            val objectList = gson.fromJson(message, TickerResponse::class.java)
            //Log.d("test", message)
//
//            runOnUiThread { output.text = "${objectList?.get(0)?.s} : ${objectList?.get(0)?.p} " }

            requireActivity().runOnUiThread { output.text = "PL Report :-${message} " }

        }
    }
}