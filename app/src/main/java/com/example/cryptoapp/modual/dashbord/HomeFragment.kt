package com.example.cryptoapp.modual.dashbord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.Constants.Companion.showToast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.model.CryptoName
import com.example.cryptoapp.modual.home.adapter.*
import com.example.cryptoapp.modual.news.response.Feed
import com.example.cryptoapp.modual.news.response.screen.NewsListActivity
import com.example.cryptoapp.modual.watchlist.WatchlistFragment
import com.example.cryptoapp.network.*
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okio.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var strategies_rv: RecyclerView
    lateinit var rv_top_coin: RecyclerView

    lateinit var ouput: TextView
    lateinit var top_coins_see_all: TextView
    lateinit var advertisement_see_all: TextView
    lateinit var top_news_see_all: TextView

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

    lateinit var client: OkHttpClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job1: Job
    private lateinit var job2: Job
    private lateinit var job3: Job
    private lateinit var job4: Job
    private val THREAD_POOL_SIZE = 10
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    lateinit var webSocket2: WebSocket
    private lateinit var fragmentContext: Context
    lateinit var first: ArrayList<CryptoName>
    val airQualityDatalist = ArrayList<AirQualityData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        preferences = MyPreferences(fragmentContext)

        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)
        // showLog("HomeFragment",Gson().toJson(data))

        first = ArrayList()
        strategies_rv = view.findViewById(R.id.strategies_rv)
        rv_top_coin = view.findViewById(R.id.rv_top_coin)
        top_news_see_all = view.findViewById(R.id.top_news_see_all)

        ouput = view.findViewById(R.id.ouput)
        top_coins_see_all = view.findViewById(R.id.top_coins_see_all)
        advertisement_see_all = view.findViewById(R.id.advertisement_see_all)
        txt_pl_price = view.findViewById(R.id.txt_pl_price)
        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        login_ViewPager = view.findViewById(R.id.login_ViewPager)
        view_pager_news = view.findViewById(R.id.view_pager_news)

        txt_strategies_desc = view.findViewById(R.id.txt_strategies_desc)

        top_coins_see_all.setOnClickListener(this)
        advertisement_see_all.setOnClickListener(this)
        top_news_see_all.setOnClickListener(this)

        setupAnim()

        executorService.submit {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MINUTES).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
                // getCMSList()
            }

            job2 = CoroutineScope(Dispatchers.IO).launch {
//                viewLoader.visibility = View.VISIBLE
                getStrategy()
            }

            job3 = CoroutineScope(Dispatchers.IO).launch {
                webSocket2 = createWebSocket("wss://fstream.binance.com/ws/!ticker@arr")
            }

            job4 = CoroutineScope(Dispatchers.IO).launch {
                getNewsList()
            }

            CoroutineScope(Dispatchers.IO).launch {

                job1.join()
                job2.join()
                job3.join()
                job4.join()

            }
        }
    }

    private fun createWebSocket(url: String): WebSocket {
        //viewLoader.visibility = View.VISIBLE
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send(data.userId)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                setUpBtcPriceText(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                showLog("WebSocket connection to $url failed", t.message.toString())
            }

        }

        return client.newWebSocket(request, listener)

    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket2.close(1000, "Activity destroyed")
        job1.cancel() // Cancel the coroutine job when the activity is destroyed
        job2.cancel()
        job3.cancel()
        job4.cancel()
        scope.cancel()
        //cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    fun setUpPriceText(message: String?) {
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

            val response = ServiceBuilder(fragmentContext, false).buildService(RestApi::class.java)
                .getCmsAdsList(data.userId)

            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE
                viewPagerAdapter = SliderViewPagerAdapter(fragmentContext, response.body()!!.data)
                login_ViewPager.adapter = viewPagerAdapter

            }
        }

    }

    private fun getNewsList() {

        val url = "https://rss.app/feeds/v1.1/tdm707wAO1CzfXTq.json"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val json = response.body?.string()

                val gson = Gson()
                val data = gson.fromJson(json, Feed::class.java)

                activity?.runOnUiThread {
                    sliderNewsViewPagerAdapter =
                        SliderNewsViewPagerAdapter(fragmentContext, data.items)
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

    fun setUpBtcPriceText(message: String?) {
        message?.let {
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

                            for ((i, item) in airQualityDatalist.withIndex()) {
                                if (dto.s.equals(item.name)) {
                                    airQualityDatalist[i] = AirQualityData(dto.s, dto.c, "1")

                                    isAvailable = true
                                    break
                                } else {
                                    isAvailable = false
                                }
                            }

                            if (isAvailable == false) {
                                airQualityDatalist.add(AirQualityData(dto.s, dto.c, "1"))
                            }

                        } else {
                            airQualityDatalist.add(AirQualityData(dto.s, dto.c, "1"))
                        }

                }
            }

            isAvailable = false

            activity?.runOnUiThread {

                rv_top_coin.layoutManager =
                    LinearLayoutManager(fragmentContext, LinearLayoutManager.HORIZONTAL, false)
                topCoinAdapter = TopCoinAdapter(fragmentContext, airQualityDatalist)
                rv_top_coin.adapter = topCoinAdapter

            }

        }
    }

    private fun getStrategy() {
        lifecycleScope.launch(Dispatchers.IO) {

            val response =
                ServiceBuilder(fragmentContext, false).buildService(RestApi::class.java)
                    .getStrategy()
            withContext(Dispatchers.Main) {

                if (response.body()!!.isSuccess == true) {
                    viewLoader.visibility = View.GONE
                    strategies_rv.layoutManager = LinearLayoutManager(fragmentContext)
                    homeAdapter = HomeAdapter(fragmentContext, response.body()!!.data, false)
                    strategies_rv.adapter = homeAdapter
                } else {
                    viewLoader.visibility = View.GONE
                    showToast(
                        fragmentContext,
                        requireActivity(),
                        getString(R.string.data_not_found)
                    )
                }

            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.top_coins_see_all -> {
                loadFragment(WatchlistFragment())
            }
            R.id.advertisement_see_all -> {
                showToast(requireActivity(), requireActivity(), "ok")
            }

            R.id.top_news_see_all -> {
                var intent = Intent(requireContext(), NewsListActivity::class.java)
                startActivity(intent)

            }


        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        // transaction.addToBackStack(null)
        transaction.commit()
    }

}
