package com.example.cryptoapp.modual.dashbord

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
import com.example.cryptoapp.Constants
import com.example.cryptoapp.Constants.Companion.showLog
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.*
import com.example.cryptoapp.modual.home.HomeDetailActivity
import com.example.cryptoapp.modual.home.adapter.*
import com.example.cryptoapp.network.*
import com.example.cryptoapp.preferences.MyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), View.OnClickListener {

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

    lateinit var strategyResList: ArrayList<StrategyWSResponseItem>
    lateinit var topCoin: ArrayList<TopCoins>
    lateinit var imageList: ArrayList<NewsData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        preferences = MyPreferences(requireContext())

        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        strategies_rv = view.findViewById(R.id.strategies_rv)
        rv_top_coin = view.findViewById(R.id.rv_top_coin)

        ouput = view.findViewById(R.id.ouput)
        txt_pl_price = view.findViewById(R.id.txt_pl_price)
        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        login_ViewPager = view.findViewById(R.id.login_ViewPager)
        view_pager_news = view.findViewById(R.id.view_pager_news)

        txt_strategies_desc = view.findViewById(R.id.txt_strategies_desc)

        strategyResList = ArrayList()
        topCoin = ArrayList()

        topCoin.add(TopCoins("", "Bitcoin", "BTC", "1000"))
        topCoin.add(TopCoins("", "Ethereum", "BTC", "2000"))
        topCoin.add(TopCoins("", "Therum", "BTC", "3000"))

        strategyResList.add(
            StrategyWSResponseItem(
                10.00,
                Strategy(
                    "1/4/2023",
                    getString(R.string.dummy_text),
                    "",
                    false,
                    10.00,
                    "7/4/2023",
                    10.00,
                    "Strategy 1"
                )
            )
        )

        // strategyResList.add(StrategyWSResponseItem(20.00,Strategy("2/4/2023",getString(R.string.dummy_text),"",false,20.00,"7/4/2023",20.00,"Strategy 2")))

//        strategies_rv.layoutManager = LinearLayoutManager(activity)
//        homeAdapter = HomeAdapter(requireContext(), strategyResList, false)
//        strategies_rv.adapter = homeAdapter

        rv_top_coin.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        topCoinAdapter = TopCoinAdapter(requireContext(), topCoin)
        rv_top_coin.adapter = topCoinAdapter

        imageList = ArrayList()
//        imageList.add(NewsData(R.drawable.ic_home, "It is a long established fact that a reader"))
//        imageList.add(NewsData(R.drawable.ic_home, "It is a long established fact that a reader"))

        sliderNewsViewPagerAdapter = SliderNewsViewPagerAdapter(requireContext(), imageList)
        view_pager_news.adapter = sliderNewsViewPagerAdapter

        setupAnim()

        executorService.submit {
            // handle WebSocket connection here
            //    scope.launch {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
//                val ws1 = async {
                showLog("IO", "1")
                //    getCMSList()
                //}
            }

            job2 = CoroutineScope(Dispatchers.IO).launch {
                //val ws2 = async {
                showLog("webSocket1", "2")
                //initGetStrategyByUser()
              //  webSocket1 = createWebSocket("ws://103.14.99.61:8084/getStrategyPL", 1)
            //        webSocket1 = createWebSocket("ws://192.168.29.76:883/getStrategyPL", 1)

                //}
            }

            job3 = CoroutineScope(Dispatchers.IO).launch {
                //val ws3 = async {
                showLog("webSocket2", "3")
                //  callFragment()
           //     webSocket2 = createWebSocket("ws://192.168.29.76:883/getPL", 2)
                //  webSocket2 = createWebSocket("wss://stream.binance.com:9443/ws/btcusdt@lastTradePrice", 2)
                // }
            }

//                ws1.await()
//                ws2.await()
//                ws3.await()

            CoroutineScope(Dispatchers.IO).launch {
                job1.join() // Wait for job1 to complete before sending message to ws2
                job2.join() // Wait for job2 to complete before sending message to ws1
                job3.join() // Wait for job2 to complete before sending message to ws1
//                webSocket1.send(data.userId)
//                webSocket2.send(data.userId)
            }

            txt_strategies_desc.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val intent = Intent(context, HomeDetailActivity::class.java)
                    //intent.putExtra("strategyId", strategyResList.get(position).Strategy.Id)
                    startActivity(intent)
                }

            })
            //}
        }
    }

    private fun createWebSocket(url: String, value: Int): WebSocket {
        //viewLoader.visibility = View.VISIBLE
        val request = Request.Builder().url(url).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                if (value == 1) {
                    webSocket.send(data.userId)
                } else if (value == 2) {
                    webSocket.send(data.userId)
                }

            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                if (value == 1) {
                    // viewLoader.visibility = View.GONE
                    setGetStrategyByUser(text)
                } else if (value == 2) {
                    //   viewLoader.visibility = View.GONE
                    setUpBtcPriceText(text)
                }

            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket disconnected from $url")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket connection to $url failed: ${t.message}")
            }

        }

        return client.newWebSocket(request, listener)
    }


    override fun onDestroy() {
        super.onDestroy()

//        webSocket1.close(1000, "Activity destroyed")
//        webSocket2.close(1000, "Activity destroyed")
        job1.cancel() // Cancel the coroutine job when the activity is destroyed
        job2.cancel()
        job3.cancel()
        scope.cancel()
        //cancel()
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

    suspend fun initGetStrategyByUser() {

        webSocketListener = object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                showLog("test${"reason"}", reason)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                super.onFailure(webSocket, t, response)
                showLog("test", t.message.toString())
                showLog("test${"messageByUser"}", t.message.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                setGetStrategyByUser(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                showLog("test${"onMessage2"}", bytes.hex())
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                webSocket.send(data.userId)
                showLog("data.userId", data.userId)
            }

        }

        //val request: Request = Request.Builder().url("wss://stream.binance.com:443/ws/!ticker@arr").build()
        val request: Request = Request.Builder()
            .url("ws://192.168.29.76:883/getStrategyPL").build()

        val ws = client!!.newWebSocket(request, webSocketListener)
        client!!.dispatcher.executorService.shutdown()

    }

    fun setGetStrategyByUser(message: String?) {
        message?.let {
            // Thread(Runnable {
            requireActivity().runOnUiThread {

                val gson = Gson()
                val objectList = gson.fromJson(message, StrategyWSResponse::class.java)

                showLog("test", objectList.toString())

                viewLoader.visibility = View.GONE
                strategies_rv.layoutManager = LinearLayoutManager(activity)
                homeAdapter =
                    context?.let { it1 -> HomeAdapter(it1, objectList, data.haveAnySubscription) }!!
                strategies_rv.adapter = homeAdapter

            }
            // }).start()
        }
    }

    fun setUpBtcPriceText(message: String?) {
        message?.let {

//            val gson = Gson()
//            val objectList = gson.fromJson(message, TickerResponse::class.java)
            //Log.d("test", message)
//
//            runOnUiThread { output.text = "${objectList?.get(0)?.s} : ${objectList?.get(0)?.p} " }

            requireActivity().runOnUiThread { txt_pl_price.text = "${message} " }

        }
    }

    suspend fun getCMSList() {
        //   viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {

            var response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
                .getCmsAdsList(data.userId)

            withContext(Dispatchers.Main) {
                //         viewLoader.visibility = View.GONE
                viewPagerAdapter = SliderViewPagerAdapter(requireContext(), response.body()!!.data)
                login_ViewPager.adapter = viewPagerAdapter

            }
        }
    }

    private fun setupAnim() {
        animationView.setAnimation(R.raw.currency)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private fun getStrategy() {

//        viewLoader.visibility = View.VISIBLE
//        lifecycleScope.launch(Dispatchers.IO) {
//            var response = ServiceBuilder(requireContext()).buildService(RestApi::class.java).getStrategy()
//            withContext(Dispatchers.Main) {
//                viewLoader.visibility = View.GONE
//                strategies_rv.layoutManager = LinearLayoutManager(requireContext())
//                homeAdapter = HomeAdapter(requireContext(), response.body()!!)
//                strategies_rv.adapter = homeAdapter
//            }
//        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }
}
