package com.example.cryptoapp.modual.dashbord

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.StrategyWSResponse
import com.example.cryptoapp.Response.TickerResponse
import com.example.cryptoapp.model.CryptoName
import com.example.cryptoapp.modual.home.adapter.*
import com.example.cryptoapp.modual.news.response.Feed
import com.example.cryptoapp.network.RestApi
import com.example.cryptoapp.network.ServiceBuilder
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okio.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HomeWithWSFragment : Fragment() , View.OnClickListener {
    lateinit var strategies_rv: RecyclerView
    lateinit var rv_top_coin: RecyclerView

    lateinit var ouput: TextView
    lateinit var txt_pl_price: TextView
    lateinit var txt_strategies_desc: TextView
    lateinit var homeAdapter: HomeAdapter
    lateinit var topCoinAdapter: TopCoinAdapter
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    lateinit var preferences: MyPreferences

    lateinit var login_ViewPager: ViewPager
    lateinit var view_pager_news: ViewPager
    lateinit var viewPagerAdapter: SliderViewPagerAdapter
    lateinit var sliderNewsViewPagerAdapter: SliderNewsViewPagerAdapter

    lateinit var data: DataXX

    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job1: Job
    private lateinit var job2: Job
    private lateinit var job3: Job
    private val THREAD_POOL_SIZE = 10
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    lateinit var webSocket1: WebSocket
    lateinit var webSocket2: WebSocket
    private lateinit var fragmentContext: Context
    lateinit var first: ArrayList<CryptoName>
    val airQualityDatalist = ArrayList<AirQualityData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        preferences = MyPreferences(fragmentContext)

        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        first = ArrayList()
        strategies_rv = view.findViewById(R.id.strategies_rv)
        rv_top_coin = view.findViewById(R.id.rv_top_coin)

        ouput = view.findViewById(R.id.ouput)
        txt_pl_price = view.findViewById(R.id.txt_pl_price)
        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        login_ViewPager = view.findViewById(R.id.login_ViewPager)
        view_pager_news = view.findViewById(R.id.view_pager_news)

        txt_strategies_desc = view.findViewById(R.id.txt_strategies_desc)


        setupAnim()

        executorService.submit {
            // handle WebSocket connection here
            //    scope.launch {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MINUTES).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
                getNewsList()
            }

            job2 = CoroutineScope(Dispatchers.IO).launch {
                webSocket1 = createWebSocket("ws://103.14.99.42/getStrategyPL", 1)
            }

            job3 = CoroutineScope(Dispatchers.IO).launch {
//                webSocket2 = createWebSocket("wss://fstream.binance.com:443/ws/!ticker@arr", 2)
                webSocket2 = createWebSocket("wss://fstream.binance.com/ws/!ticker@arr", 2)
            }

            CoroutineScope(Dispatchers.IO).launch {
                job1.join() // Wait for job1 to complete before sending message to ws2
                job2.join() // Wait for job2 to complete before sending message to ws1
                job3.join() // Wait for job2 to complete before sending message to ws1

            }

        }
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
                    viewLoader.visibility = View.GONE
                    setGetStrategyByUser(text)
                } else if (value == 2) {
                    setUpBtcPriceText(text, value)
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

    override fun onDestroy() {
        super.onDestroy()
        webSocket1.close(1000, "Activity destroyed")
        webSocket2.close(1000, "Activity destroyed")
        job1.cancel() // Cancel the coroutine job when the activity is destroyed
        job2.cancel()
        job3.cancel()
        scope.cancel()
        //cancel()
    }

    fun setGetStrategyByUser(message: String?) {
        message?.let {
            requireActivity().runOnUiThread {

                val gson = Gson()
                val objectList = gson.fromJson(message, StrategyWSResponse::class.java)

                if (!objectList.TotalPL.toString().isEmpty()) {
                    setUpBtcPriceText(objectList.TotalPL!!.toString())
                }

//                 showLog("test", Gson().toJson(objectList))

//                viewLoader.visibility = View.GONE
//                strategies_rv.layoutManager = LinearLayoutManager(activity)
//                homeAdapter = HomeAdapter(fragmentContext, objectList.StrategyPLVMs, false)!!
//                strategies_rv.adapter = homeAdapter

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    fun setUpBtcPriceText(message: String?) {
        message?.let {

//            val gson = Gson()
//            val objectList = gson.fromJson(message, TickerResponse::class.java)
            //Log.d("test", message)
//
//            runOnUiThread { output.text = "${objectList?.get(0)?.s} : ${objectList?.get(0)?.p} " }

            txt_pl_price.text = "${message} "

        }
    }

    suspend fun getCMSList() {
        //   viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {

            var response = ServiceBuilder(fragmentContext).buildService(RestApi::class.java)
                .getCmsAdsList(data.userId)

            withContext(Dispatchers.Main) {
                //         viewLoader.visibility = View.GONE
                viewPagerAdapter = SliderViewPagerAdapter(fragmentContext, response.body()!!.data)
                login_ViewPager.adapter = viewPagerAdapter

            }
        }
    }
    private fun getNewsList() {

//        imageList = ArrayList()
//        imageList.add(NewsData(R.drawable.ic_home, "It is a long established fact that a reader"))
//        imageList.add(NewsData(R.drawable.ic_home, "It is a long established fact that a reader"))

        val url = "https://rss.app/feeds/v1.1/7nFKrhvpzsUtzAaz.json"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val json = response.body?.string()

                // Parse JSON using Gson
                val gson = Gson()
                val data = gson.fromJson(json, Feed::class.java)
                Constants.showLog("NewsResponse", Gson().toJson(data))
                // Now you can work with the parsed JSON data
                // For example, update UI elements using runOnUiThread

                requireActivity().runOnUiThread {
//                     Update UI here with 'data'
                    sliderNewsViewPagerAdapter = SliderNewsViewPagerAdapter(fragmentContext, data.items)
                    view_pager_news.adapter = sliderNewsViewPagerAdapter
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    fun setUpBtcPriceText(message: String?, value: Int) {
        message?.let {

            if (value == 2) {
                val gson = Gson()
                first.add(CryptoName("ETHUSDT"))
                first.add(CryptoName("BTCUSDT"))
                first.add(CryptoName("XRPUSDT"))
//                first.add(CryptoName("MCUSDT"))
//                first.add(CryptoName("BNBBTC"))
//                first.add(CryptoName("NEOBTC"))
//                first.add(CryptoName("LTCBTC"))
//                first.add(CryptoName("EOSBTC"))

                val objectList = gson.fromJson(message, TickerResponse::class.java)
                // showLog("objectList", objectList.toString())

                var isAvailable = false
                objectList.mapIndexed { index, dto ->
                    for (person in first) {
                        if (dto.s.equals(person.name))
                            if (airQualityDatalist.size != 0) {

                                for ((i, value) in airQualityDatalist.withIndex()) {
                                    if (dto.s.equals(value.name)) {
                                        airQualityDatalist[i] = AirQualityData(dto.s, dto.c)

                                        isAvailable = true
                                        break
                                    } else {
                                        isAvailable = false
                                    }
                                }

                                if (isAvailable == false) {
                                    airQualityDatalist.add(AirQualityData(dto.s, dto.c))
                                }

                            } else {
                                airQualityDatalist.add(AirQualityData(dto.s, dto.c))
                            }
                    }
                }

                isAvailable = false

                requireActivity().runOnUiThread {

                    rv_top_coin.layoutManager =
                        LinearLayoutManager(fragmentContext, LinearLayoutManager.HORIZONTAL, false)
                    topCoinAdapter = TopCoinAdapter(fragmentContext, airQualityDatalist)
                    rv_top_coin.adapter = topCoinAdapter

                }

            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }
}
