package com.example.cryptoapp.modual.dashbord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var strategies_rv: RecyclerView
    lateinit var homeAdapter: HomeAdapter
    lateinit var viewLoader: View
    lateinit var animationView: LottieAnimationView
    lateinit var preferences: MyPreferences

    lateinit var login_ViewPager: ViewPager
    lateinit var viewPagerAdapter: SliderViewPagerAdapter

    lateinit var data: DataXX

    lateinit var webSocketListener: WebSocketListener
    lateinit var client: OkHttpClient

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
        client = OkHttpClient()
        data = Gson().fromJson(preferences.getLogin(), DataXX::class.java)

        strategies_rv = view.findViewById(R.id.strategies_rv)
        viewLoader = view.findViewById(R.id.viewLoader)
        animationView = viewLoader.findViewById(R.id.lotti_img)

        login_ViewPager = view.findViewById(R.id.login_ViewPager)

        setupAnim()

        CoroutineScope(Dispatchers.IO).launch {
            showLog("IO", "1")
            getCMSList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            showLog("Main", "3")
            initGetStrategyByUser()
        }

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
                strategies_rv.layoutManager = LinearLayoutManager(requireContext())
                homeAdapter = HomeAdapter(requireContext(), objectList)
                strategies_rv.adapter = homeAdapter
            }
          // }).start()
        }
    }

    suspend fun getCMSList() {
        viewLoader.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {

            var response = ServiceBuilder(requireContext()).buildService(RestApi::class.java)
                .getCmsAdsList(data.userId)

            withContext(Dispatchers.Main) {
                viewLoader.visibility = View.GONE
                viewPagerAdapter = SliderViewPagerAdapter(requireContext(), response.body()!!.data)
                login_ViewPager.adapter = viewPagerAdapter

                CoroutineScope(Dispatchers.Main).launch {
                    showLog("Main", "2")
                    callFragment()
                }
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

