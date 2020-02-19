package com.prasad.blockchain

import android.os.Handler
import android.os.Message
import okhttp3.*
import java.util.concurrent.TimeUnit


class ServerConnection(serverUrl: String) {

    private var mWebSocket: WebSocket? = null
    private var mClient: OkHttpClient? = null
    private var mServerUrl: String? = null
    private var mMessageHandler: Handler? = null
    private var mStatusHandler: Handler? = null
    private var mListener: ServerListener? = null

    enum class ConnectionStatus {
        DISCONNECTED, CONNECTED
    }

    init {
        mClient = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        mServerUrl = serverUrl
    }

    interface ServerListener {
        fun onNewMessage(message: String?)
        fun onStatusChange(status: ConnectionStatus?)
    }

    inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            val m: Message? = mMessageHandler?.obtainMessage(0, ConnectionStatus.CONNECTED)
            mStatusHandler?.sendMessage(m)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val m: Message? = mMessageHandler?.obtainMessage(0, text)
            mMessageHandler?.sendMessage(m)
        }

        override fun onClosed(
            webSocket: WebSocket,
            code: Int,
            reason: String
        ) {
            val m: Message? = mStatusHandler?.obtainMessage(0, ConnectionStatus.DISCONNECTED)
            mStatusHandler?.sendMessage(m)
        }

        override fun onFailure(
            webSocket: WebSocket?,
            t: Throwable?,
            response: Response?
        ) {
            disconnect()
        }
    }

    fun connect(listener: ServerListener) {
        val request = Request.Builder()
            .url(mServerUrl)
            .build()
        mWebSocket = mClient!!.newWebSocket(request, EchoWebSocketListener())
        mListener = listener
        mMessageHandler =
            Handler(Handler.Callback { msg: Message ->
                mListener!!.onNewMessage(msg.obj as String)
                true
            })
        mStatusHandler = Handler(Handler.Callback { msg: Message ->
            mListener!!.onStatusChange(msg.obj as ConnectionStatus)
            true
        })
    }

    fun disconnect() {
        mWebSocket!!.cancel()
        mListener = null
        mMessageHandler!!.removeCallbacksAndMessages(null)
        mStatusHandler!!.removeCallbacksAndMessages(null)
    }

    fun sendMessage(message: String?) {
        mWebSocket?.send(message)
    }

}