package com.example.cryptoapp.modual.watchlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.TickerResponse
import com.example.cryptoapp.Response.TickerResponseItem
import com.example.cryptoapp.modual.dashbord.PLFragment
import com.example.cryptoapp.modual.home.adapter.AirQualityData
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.modual.watchlist.adapter.WatchlistAdapter
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*

import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class WatchlistFragment : Fragment() {

    lateinit var rv_watchlist: RecyclerView
    lateinit var rv_watchlist1: RecyclerView
    lateinit var rv_watchlist2: RecyclerView
    lateinit var view_pager: FrameLayout

    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    lateinit var watchlistAdapter: WatchlistAdapter
    lateinit var first: ArrayList<String>
    lateinit var tickerResponseItem: TickerResponseItem
    lateinit var airQualityData: AirQualityData

    private val scope = CoroutineScope(Dispatchers.Main)
    lateinit var webSocket1: WebSocket
    lateinit var webSocket2: WebSocket
    lateinit var webSocket3: WebSocket
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)

        client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()
        rv_watchlist = view.findViewById(R.id.rv_watchlist)
        rv_watchlist1 = view.findViewById(R.id.rv_watchlist1)
        rv_watchlist2 = view.findViewById(R.id.rv_watchlist2)
        view_pager = view.findViewById(R.id.view_pager)

        first = ArrayList()

        scope.launch {

            val ws1 = async {
                showLog("IO", "1")
                webSocket1 = createWebSocket("wss://stream.binance.com:443/ws/!ticker@arr", 1)
            }

//            val ws2 = async {
//                Constants.showLog("IO", "2")
//                webSocket2 = createWebSocket("wss://stream.binance.com:443/ws/!ticker@arr", 2)
//            }
//
//            val ws3 = async {
//                Constants.showLog("IO", "3")
//                webSocket3 = createWebSocket("wss://stream.binance.com:443/ws/!ticker@arr", 3)
//            }
//
//            val ws4 = async {
//                Constants.showLog("IO", "4")
//                callFragment()
//            }
//

            ws1.await()
//            ws2.await()
//            ws3.await()
//            ws4.await()
        }

        return view
    }


    suspend fun callFragment() {
        // Thread(Runnable {
        // performing some dummy time taking operation

//        val mainLooper = Looper.getMainLooper()
//        Handler(mainLooper).post {
//        requireActivity().runOnUiThread {
//            val fragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.view_pager, PLFragment()).commit()
//        }

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.view_pager, PLFragment()).commit()
            }
        }

        //}
        //}).start()
    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
        val request: Request = Request.Builder()
            //.url("wss://fstream.binance.com/ws/bnbusdt@aggTrade?ApiKey=ISXXV72Gjq4OUE3K6vssFe68R0daLiKWwAly5459nUw7PY0vuIG1sHgWRLGafuYM&ApiSecret=zQIlAYwQ2mwZKl5ERgCXLJ1aQ2fuFejlpDU73fKvoEzmGaj4RWE7gPAgDgwa1hzM")
            .url(url)
            .build()
        webSocketListener = object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                showLog("onClosing", reason)

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                showLog("t.message.toString()", t.message.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                setUpBtcPriceText(text, value)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                showToast(requireContext(), bytes.hex())
                showLog("bytes.hex()", bytes.hex())
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

//
//        val ws = client!!.newWebSocket(request, webSocketListener)
//        client!!.dispatcher.executorService.shutdown()

        return client.newWebSocket(request, webSocketListener)

    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket1.close(1000, "Activity destroyed 1")
//        webSocket2.close(1000, "Activity destroyed 2")
//        webSocket3.close(1000, "Activity destroyed 3")

        scope.cancel()
        //cancel()
    }

    fun setUpBtcPriceText(message: String?, value: Int) {
        message?.let {

            if (value == 1) {
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
                            showLog("objectList", "${index} :- ${value} = ${dto.s}")
                        }
                    }
                }

                requireActivity().runOnUiThread {
                    rv_watchlist.layoutManager = LinearLayoutManager(activity)
                    watchlistAdapter = context?.let { it1 -> WatchlistAdapter(it1, list) }!!
                    rv_watchlist.adapter = watchlistAdapter
                }

//            } else if (value == 2) {
//                val gson = Gson()
//                first.add("ETHIC")
//                first.add("MAGICBTC")
//                first.add("MCUSDT")
//
//                val objectList = gson.fromJson(message, TickerResponse::class.java)
//                val list = ArrayList<AirQualityData>()
//
//                objectList.map { dto ->
//                    for ((index, value) in first.withIndex()) {
//                        if (dto.s.equals(value)) {
//                            airQualityData = AirQualityData(dto.s, dto.p)
//                            list.add(airQualityData)
//                            Log.d("test", "${index} :- ${value} = ${dto.s}")
//                        }
//                    }
//                }
//
//                requireActivity().runOnUiThread {
//                    rv_watchlist1.layoutManager = LinearLayoutManager(requireContext())
//                    watchlistAdapter = WatchlistAdapter(requireContext(), list)
//                    rv_watchlist1.adapter = watchlistAdapter
//                }
//
//            } else if (value == 3) {
//                val gson = Gson()
//                first.add("ETHBTC")
//                first.add("MAGICBTC")
//                first.add("MCUSDT")
//
//                val objectList = gson.fromJson(message, TickerResponse::class.java)
//                val list = ArrayList<AirQualityData>()
//
//                objectList.map { dto ->
//                    for ((index, value) in first.withIndex()) {
//                        if (dto.s.equals(value)) {
//                            airQualityData = AirQualityData(dto.s, dto.p)
//                            list.add(airQualityData)
//                            Log.d("test", "${index} :- ${value} = ${dto.s}")
//                        }
//                    }
//                }

//                requireActivity().runOnUiThread {
//                    rv_watchlist2.layoutManager = LinearLayoutManager(requireContext())
//                    watchlistAdapter = WatchlistAdapter(requireContext(), list)
//                    rv_watchlist2.adapter = watchlistAdapter
//                }

            }
        }
    }
}
