package com.prasad.blockchain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prasad.blockchain.ServerConnection.ConnectionStatus
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class MainActivity : AppCompatActivity(), ServerConnection.ServerListener {

    private lateinit var client: OkHttpClient
    private val SERVER_URL = "wss://ws.blockchain.info/inv"
    private lateinit var mServerConnection: ServerConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mServerConnection = ServerConnection(SERVER_URL)
        //client = OkHttpClient()
        //onStartConnect()

    }

    override fun onResume() {
        super.onResume()
        mServerConnection.connect(this)
        mServerConnection.sendMessage("{\"op\": \"ping_block\"}")
        mServerConnection.sendMessage("{\"op\": \"ping_tx\"}")
    }

    override fun onPause() {
        super.onPause()
        mServerConnection.disconnect()
    }

    /*private fun onStartConnect() {
        val request =
            Request.Builder().url("wss://ws.blockchain.info/inv").build()
        val listener = EchoWebSocketListenerTest()
        val ws = client.newWebSocket(request, listener)
        println("listner ws : $ws")
        client.dispatcher().executorService().shutdown()
    }*/

    override fun onNewMessage(message: String?) {
        println("message : $message")
        var obj = JSONObject(message)
        if(obj.has("op") && obj.optString("op") == "block"){
            println("message op: ${obj.optString("op")}")
            tvBlockHash.text = "Block Hash : " + obj.optString("hash")
            tvBlockHeight.text = "Block Height :  " + obj.optInt("height")
            tvTotalBtcSent.text = "Total BTC Sent : " + obj.optInt("totalBTCSent")
            tvReward.text = "Reward : " + obj.optInt("reward")
        }
    }

    override fun onStatusChange(status: ConnectionStatus?) {
        val statusMsg =
            if (status === ConnectionStatus.CONNECTED) getString(R.string.connected) else getString(
                R.string.disconnected
            )
        mConnectionStatus.text = statusMsg
    }
}
