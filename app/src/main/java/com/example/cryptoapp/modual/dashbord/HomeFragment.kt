package com.example.cryptoapp.modual.dashbord

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
import com.example.cryptoapp.R
import com.example.cryptoapp.Response.DataXX
import com.example.cryptoapp.Response.StrategyWSResponse
import com.example.cryptoapp.modual.home.adapter.HomeAdapter
import com.example.cryptoapp.modual.home.adapter.SliderViewPagerAdapter
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
    lateinit var ouput: TextView
    lateinit var homeAdapter: HomeAdapter
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    lateinit var preferences: MyPreferences

    lateinit var login_ViewPager: ViewPager
    lateinit var viewPagerAdapter: SliderViewPagerAdapter

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
        ouput = view.findViewById(R.id.ouput)
        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        login_ViewPager = view.findViewById(R.id.login_ViewPager)

        setupAnim()
        executorService.submit {
            // handle WebSocket connection here
            //    scope.launch {

            client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

            job1 = CoroutineScope(Dispatchers.Main).launch {
//                val ws1 = async {
                showLog("IO", "1")
               getCMSList()
                //}
            }

            job2 = CoroutineScope(Dispatchers.IO).launch {
                //val ws2 = async {
                showLog("webSocket1", "2")
                //initGetStrategyByUser()
                webSocket1 = createWebSocket("ws://103.14.99.61:8084/getStrategyPL", 1)

                //}
            }

            job3 = CoroutineScope(Dispatchers.IO).launch {
                //val ws3 = async {
                showLog("webSocket2", "3")
                //  callFragment()
                webSocket2 = createWebSocket("ws://103.14.99.61:8084/getPL", 2)
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
              //  webSocket1.send(data.userId)
                //webSocket2.send(data.userId)
            }

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
                }

//                else if (value == 2) {
//                    webSocket.send(data.userId)
//                }

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

        webSocket1.close(1000, "Activity destroyed")
        webSocket2.close(1000, "Activity destroyed")
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
            .url("ws://103.14.99.61:8084/getStrategyPL").build()
        val ws = client!!.newWebSocket(request, webSocketListener)
        client!!.dispatcher.executorService.shutdown()

    }

    fun setGetStrategyByUser(message: String?) {
        message?.let {
            // Thread(Runnable {
            requireActivity().runOnUiThread {
                val gson = Gson()
                val objectList = gson.fromJson(message, StrategyWSResponse::class.java)
                showLog("message", message)
                viewLoader.visibility = View.GONE
                strategies_rv.layoutManager = LinearLayoutManager(activity)
                homeAdapter = context?.let { it1 -> HomeAdapter(it1, objectList) }!!
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

            requireActivity().runOnUiThread { ouput.text = "PL Report :-${message} " }

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
