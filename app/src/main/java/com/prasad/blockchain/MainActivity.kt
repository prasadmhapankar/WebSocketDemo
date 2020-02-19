package com.prasad.blockchain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = OkHttpClient()
        onStartConnect()

    }

    private fun onStartConnect() {
        val request =
            Request.Builder().url("wss://ws.blockchain.info/inv").build()
        val listener = EchoWebSocketListener()
        val ws = client.newWebSocket(request, listener)
        println("listner ws : $ws")
        client.dispatcher().executorService().shutdown()
    }
}
