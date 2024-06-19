package com.bangkit.hear4u.services

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

object SocketManager {
    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://34.101.212.39:8000/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        mSocket?.connect()
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on("predict_audio", onMessage)
    }

    fun disconnect() {
        mSocket?.disconnect()
    }

    fun emit(event: String, data: Any) {
        mSocket?.emit(event, data)
    }
    fun on(event: String, listener: Emitter.Listener) {
        mSocket?.on(event, listener)
    }
    fun off(event: String, listener: Emitter.Listener) {
        mSocket?.off(event, listener)
    }

    private val onConnect = Emitter.Listener {
        Log.d("SocketManager", "Connected to server")
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("SocketManager", "Disconnected from server")
    }

    private val onMessage = Emitter.Listener { args ->
        if (args[0] != null) {
            val data = args[0] as JSONObject
            Log.d("SocketManager", "Message from server: $data")
        }
    }
}