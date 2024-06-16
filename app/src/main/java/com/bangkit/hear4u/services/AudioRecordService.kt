package com.bangkit.hear4u.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

import java.util.concurrent.Executors

class AudioRecordService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val SAMPLE_RATE = 44100
    private val TAG = "Audio Service"
    private val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
    private val audioBuffer = ShortArray(bufferSize)

    private val accumulatedAudio = mutableListOf<Short>()


    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        SocketManager.connect()
        SocketManager.on("response", onServerResponse)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AudioService", "Sedang Merekam")
        serviceScope.launch {
            startRecording()
        }
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        stopRecording()
        Log.d("AudioService", "onDestroy: Service dihentikan")
    }

    //Start Recording
    private fun startRecording() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize)

        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord initialization failed")
            stopSelf()
            return
        }

        audioRecord?.startRecording()
        isRecording = true

        Executors.newSingleThreadExecutor().execute {
            while (isRecording) {
                val read = audioRecord?.read(audioBuffer, 0, audioBuffer.size) ?: 0
                if (read > 0) {
                    accumulatedAudio.addAll(audioBuffer.take(read))
                    if (accumulatedAudio.size >= SAMPLE_RATE * 4) {
                        // Process the audio buffer here
                        sendAudioData(accumulatedAudio.toShortArray())
                        accumulatedAudio.clear()
                    }
                }
            }
        }

        Log.d(TAG, "Recording started")
    }

    private fun sendAudioData(buffer: ShortArray) {
        val audioData = ByteArray(buffer.size * 2)
        for (i in buffer.indices) {
            audioData[i * 2] = (buffer[i].toInt() and 0x00FF).toByte()
            audioData[i * 2 + 1] = (buffer[i].toInt() shr 8).toByte()
        }
        SocketManager.emit("predict_audio", audioData)
        Log.d(TAG, "Sent audio data: ${audioData.size} bytes")

    }

    private val onServerResponse = Emitter.Listener { args ->
        val response = args[0] as JSONObject // Assuming server response is a String
        val intent = Intent("com.example.realtime sound.SERVER_RESPONSE")
        intent.putExtra("response", response.toString())
        sendBroadcast(intent)
        Log.d(TAG, "Received server response: ${response.toString()}")
    }

    private fun stopRecording() {
        if (audioRecord != null) {
            isRecording = false
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            Log.d(TAG, "Recording stopped")
        }
    }

}