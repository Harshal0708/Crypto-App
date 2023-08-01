package com.example.cryptoapp.modual.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.modual.history.adapter.PositionAdapter
import com.example.cryptoapp.modual.watchlist.adapter.WatchlistAdapter
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class PositionFragment : Fragment() {

    private val THREAD_POOL_SIZE = 10
    private lateinit var job2: Job
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    lateinit var client: OkHttpClient
    lateinit var webSocket1: WebSocket
    lateinit var data: DataXX
    lateinit var preferences: MyPreferences
    private lateinit var fragmentContext: Context

    lateinit var rv_position: RecyclerView
    lateinit var positionAdapter: PositionAdapter
    lateinit var liveOrderResponse: ArrayList<LiveOrderResponseItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_position, container, false)

        init(view)
        return view
    }

    private fun init(view: View) {

        preferences = MyPreferences(fragmentContext)
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        rv_position = view.findViewById(R.id.rv_position)

        executorService.submit {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MINUTES).build()

            job2 = CoroutineScope(Dispatchers.IO).launch {
                webSocket1 = createWebSocket("ws://103.14.99.42/LiveOrderPL", 1)
            }
//                ws1.await()

            CoroutineScope(Dispatchers.IO).launch {
                job2.join() // Wait for job2 to complete before sending message to ws1
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
        //viewLoader.visibility = View.VISIBLE
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                if (value == 1) {
                    webSocket.send(data.userId)
                }

            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                if (value == 1) {
                    setLiveOrderPL(text)
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Constants.showLog("WebSocket connection to $url failed", t.message.toString())
            }

        }

        return client.newWebSocket(request, listener)
    }

    private fun setLiveOrderPL(text: String) {

        Constants.showLog("setLiveOrderPL", text)
        val gson = Gson()
        val objectList = gson.fromJson(text, LiveOrderResponse::class.java)
//
////        liveOrderResponse= objectList
////        Constants.showLog("setLiveOrderPL", Gson().toJson(objectList))
//
        requireActivity().runOnUiThread {
            rv_position.layoutManager = LinearLayoutManager(activity)
            positionAdapter = PositionAdapter(fragmentContext, objectList)!!
            rv_position.adapter = positionAdapter
        }
    }
}