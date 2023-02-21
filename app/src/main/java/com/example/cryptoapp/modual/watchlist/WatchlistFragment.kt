package com.example.cryptoapp.modual.watchlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.TickerResponse
import com.example.cryptoapp.Response.TickerResponseItem
import com.example.cryptoapp.modual.home.adapter.AirQualityData
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.modual.watchlist.adapter.WatchlistAdapter
import com.google.gson.Gson
import okhttp3.*

import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class WatchlistFragment : Fragment() {

    lateinit var rv_watchlist: RecyclerView
    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    lateinit var watchlistAdapter: WatchlistAdapter
    lateinit var first: ArrayList<String>
    lateinit var tickerResponseItem: TickerResponseItem
    lateinit var airQualityData: AirQualityData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)

        client = OkHttpClient()
        rv_watchlist = view.findViewById(R.id.rv_watchlist)
        first = ArrayList()

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
                Log.d("test${"reason"}", reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("test", t.message.toString())
                Log.d("test${"message"}", t.message.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                setUpBtcPriceText(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("test${"onMessage2"}", bytes.hex())
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
//                val jsonObject = JSONObject()
//                val jsonArray = JSONArray()
//                jsonArray.put("btcusdt@aggTrade")
//                jsonArray.put("btcusdt@depth")
//
//                try {
//                    jsonObject.put("method", "SUBSCRIBE")
//
//                    jsonObject.put("params", jsonArray)
//                    jsonObject.put("id", 1)
//                    webSocket!!.send(jsonObject.toString())
//
//                    Log.d("test${"jsonObject"}", jsonObject.toString())
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }

            }
        }

        val request: Request = Request.Builder()
            //.url("wss://fstream.binance.com/ws/bnbusdt@aggTrade?ApiKey=ISXXV72Gjq4OUE3K6vssFe68R0daLiKWwAly5459nUw7PY0vuIG1sHgWRLGafuYM&ApiSecret=zQIlAYwQ2mwZKl5ERgCXLJ1aQ2fuFejlpDU73fKvoEzmGaj4RWE7gPAgDgwa1hzM")
            .url("wss://stream.binance.com:443/ws/!ticker@arr")
            .build()
        val ws = client!!.newWebSocket(request, webSocketListener)
        client!!.dispatcher.executorService.shutdown()
    }

    fun setUpBtcPriceText(message: String?) {
        message?.let {
            val gson = Gson()
            first.add("ETHBTC")
            first.add("MAGICBTC")
            first.add("MCUSDT")

            val objectList = gson.fromJson(message, TickerResponse::class.java)
            val list = ArrayList<AirQualityData>()

            objectList.map { dto ->
                for ((index, value) in first.withIndex()) {
                    if (dto.s.equals(value)) {
                        airQualityData = AirQualityData(dto.s, dto.p)
                        list.add(airQualityData)
                        Log.d("test", "${index} :- ${value} = ${dto.s}")
                    }
                }
            }

            requireActivity().runOnUiThread {
                rv_watchlist.layoutManager = LinearLayoutManager(requireContext())
                watchlistAdapter = WatchlistAdapter(requireContext(), list)
                rv_watchlist.adapter = watchlistAdapter
            }
        }
    }
}