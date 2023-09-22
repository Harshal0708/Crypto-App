package com.example.cryptoapp.modual.watchlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.LiveOrderResponse
import com.example.cryptoapp.Response.LiveTopGainersResponse
import com.example.cryptoapp.Response.TickerResponse
import com.example.cryptoapp.model.CryptoName
import com.example.cryptoapp.modual.home.adapter.AirQualityData
import com.example.cryptoapp.modual.watchlist.adapter.WatchlistAdapter
import com.example.cryptoapp.preferences.MyPreferences
import com.example.cryptoapp.singleton.MySingleton
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


class WatchlistFragment : Fragment() {

    lateinit var rv_watchlist: RecyclerView
    lateinit var rv_watchlist1: RecyclerView
    lateinit var rv_watchlist2: RecyclerView
    lateinit var view_pager: FrameLayout

    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    lateinit var watchlistAdapter: WatchlistAdapter
    lateinit var first: ArrayList<CryptoName>

    private val scope = CoroutineScope(Dispatchers.Main)
    lateinit var webSocket1: WebSocket
    val airQualityDatalist = ArrayList<AirQualityData>()
    private lateinit var fragmentContext: Context
    lateinit var data: DataXX
    lateinit var preferences: MyPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)
        preferences = MyPreferences(fragmentContext)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MINUTES).build()
        rv_watchlist = view.findViewById(R.id.rv_watchlist)
        rv_watchlist1 = view.findViewById(R.id.rv_watchlist1)
        rv_watchlist2 = view.findViewById(R.id.rv_watchlist2)
        view_pager = view.findViewById(R.id.view_pager)

        first = ArrayList()

        scope.launch {

            val ws1 = async {
                showLog("IO", "1")
//                webSocket1 = createWebSocket("wss://fstream.binance.com:443/ws/!ticker@arr", 1)
//                webSocket1 = createWebSocket("wss://103.14.99.42/LiveTopGainers", 1)
                webSocket1 = createWebSocket("wss://103.14.99.42/GetTopGainers", 1)
            }

            ws1.await()
        }

        return view
    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
//.url("wss://fstream.binance.com/ws/bnbusdt@aggTrade?ApiKey=ISXXV72Gjq4OUE3K6vssFe68R0daLiKWwAly5459nUw7PY0vuIG1sHgWRLGafuYM&ApiSecret=zQIlAYwQ2mwZKl5ERgCXLJ1aQ2fuFejlpDU73fKvoEzmGaj4RWE7gPAgDgwa1hzM")
        val request: Request = Request.Builder()
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
                showToast(requireContext(),requireActivity(), bytes.hex())
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
//                webSocket.send(data.userId)
            }
        }

        return client.newWebSocket(request, webSocketListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket1.close(1000, "Activity destroyed 1")
        scope.cancel()
        //cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }


    fun setUpBtcPriceText(message: String?, value: Int) {
        showLog("1111", message.toString())
//        val gson = Gson()
//        val objectList = gson.fromJson(message, LiveTopGainersResponse::class.java)
//        requireActivity().runOnUiThread {
//            rv_watchlist.layoutManager = LinearLayoutManager(fragmentContext)
//            watchlistAdapter = WatchlistAdapter(fragmentContext, objectList)
//            rv_watchlist.adapter = watchlistAdapter
//        }

//        message?.let {
//
//            if (value == 1) {
//                val gson = Gson()
//                first.add(CryptoName("BTCUSDT"))
////                first.add(CryptoName("MAGICBTC"))
////                first.add(CryptoName("ETHUSDT"))
////                first.add(CryptoName("MCUSDT"))
////                first.add(CryptoName("BNBBTC"))
////                first.add(CryptoName("NEOBTC"))
////                first.add(CryptoName("LTCBTC"))
////                first.add(CryptoName("EOSBTC"))
//
//                val objectList = gson.fromJson(message, TickerResponse::class.java)
//
//                var isAvailable = false
//
//                objectList.mapIndexed { index, dto ->
//
//                    for (person in first) {
//                        if (dto.s.equals(person.name))
//                            if (airQualityDatalist.size != 0) {
//
//                                for ((i, value) in airQualityDatalist.withIndex()) {
//
//                                    if (dto.s.equals(value.name)) {
//                                        airQualityDatalist[i] = AirQualityData(dto.s, dto.c, dto.c)
////                                        showLog("-------", "--------------------------------")
////                                        showLog("objectList", dto.c)
////                                        showLog("airQualityDatalist", value.price)
////
////
////                                        showLog("-------", "--------------------------------")
//
////                                        if (value.price.toDouble() < dto.c.toDouble()) {
////                                            airQualityDatalist[i] = AirQualityData(dto.s, dto.c, dto.c)
//////                                            val isTrue = value.price.toDouble() < dto.c.toDouble()
//////                                            showLog("1", value.price + ":" + dto.c.toDouble() + "----"  + isTrue)
////                                        } else {
////
////                                            if(value.price.toDouble() > dto.c.toDouble()){
////                                                airQualityDatalist[i] = AirQualityData(dto.s, dto.c, dto.c)
//////                                                val isTrue = value.price.toDouble() < dto.c.toDouble()
//////                                                showLog("2", value.price + ":" + dto.c.toDouble() + "----"  + isTrue)
////                                            } else {
////                                                airQualityDatalist[i] = AirQualityData(dto.s, dto.c, value.price)
//////                                                val isTrue = value.price.toDouble() < dto.c.toDouble()
//////                                                showLog("3", value.price + ":" + dto.c.toDouble() + "----"  + isTrue)
////                                            }
////
////                                        }
//
//
//
////                                        var previewPrice = MySingleton().getStoredValue()
//
////                                        if (previewPrice.equals("")) {
////                                            airQualityDatalist[i] =
////                                                AirQualityData(dto.s, dto.c, dto.c)
////                                        } else {
////                                            if (dto.c.toDouble() > previewPrice.toDouble()) {
////                                                airQualityDatalist[i] =
////                                                    AirQualityData(dto.s, dto.c, previewPrice)
////                                            } else {
////                                                airQualityDatalist[i] =
////                                                    AirQualityData(dto.s, dto.c, dto.c)
////                                            }
////                                        }
//
//
////                                        MySingleton().setStoredValue(dto.c)
//                                        isAvailable = true
//
//                                        break
//                                    } else {
//                                        isAvailable = false
//                                    }
//                                }
//
//                                if (isAvailable == false) {
//                                    airQualityDatalist.add(AirQualityData(dto.s, dto.c, "0.00"))
//                                }
//
//                            } else {
//                                airQualityDatalist.add(AirQualityData(dto.s, dto.c, "0.00"))
//                            }
//                    }
//                }
//
//                isAvailable = false
//
//                requireActivity().runOnUiThread {
//                    rv_watchlist.layoutManager = LinearLayoutManager(fragmentContext)
//                    watchlistAdapter = WatchlistAdapter(fragmentContext, airQualityDatalist)
//                    rv_watchlist.adapter = watchlistAdapter
//                }
//            }
//        }
    }
}
