package com.bangkit.hear4u.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.ActivityMainBinding
import com.bangkit.hear4u.utils.formatLabel
import org.json.JSONObject

class ServerResponseReceiver(private val binding: ActivityMainBinding) : BroadcastReceiver() {
    @SuppressLint("StringFormatInvalid")
    override fun onReceive(context: Context, intent: Intent) {
        val response = intent.getStringExtra("response")
        if (response != null) {
            try {
                val jsonResponse = JSONObject(response)
                val label = jsonResponse.getString("label")
                val formattedLabel = formatLabel(label)
                val confidence = jsonResponse.getInt("confidence")
                if (confidence > 80) {
                    binding.tvOutput.text = "Detected $formattedLabel"

                } else {
                    binding.tvOutput.text = context.getString(R.string.listening)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
