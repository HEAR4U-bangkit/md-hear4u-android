package com.bangkit.hear4u.ui.speechToText

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat

class SpeechToTextConverter(private val context: Context, private val onRecognitionListener: onRecognitionListener) {

    private val TAG_RECOGNITION = "SpeechRecognitionTag"
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            onRecognitionListener.onReadyForSpeech()
            Log.d(TAG_RECOGNITION, "onReadyForSpeech:")
        }

        override fun onBeginningOfSpeech() {
            onRecognitionListener.onBeginningOfSpeech()
            Log.d(TAG_RECOGNITION, "onBeginningOfSpeech:")
        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            onRecognitionListener.onEndOfSpeech()
            Log.d(TAG_RECOGNITION, "onEndOfSpeech:")
        }

        override fun onError(error: Int) {
            val errorMessage = getErrorText(error)
            onRecognitionListener.onError(errorMessage)
            Log.e(TAG_RECOGNITION, "onError: $errorMessage")
        }

        override fun onResults(results: Bundle?) {
            val resultArray = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!resultArray.isNullOrEmpty()) {
                try {
                    val spokenText = resultArray[0]
                    onRecognitionListener.onResults(spokenText)
                } catch (ex: Exception) {
                    onRecognitionListener.onError("${ex.message}")
                    Log.e(TAG_RECOGNITION, "${ex.message}")
                }
            } else {
                onRecognitionListener.onError("List is Empty or Null")
                Log.e(TAG_RECOGNITION, "List is Empty or Null")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    fun startListening(language: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onRecognitionListener.onError("Microphone permission not granted")
            Log.e(TAG_RECOGNITION, "Microphone permission not granted")
            return
        }

        try {
            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(speechRecognizerIntent)
        } catch (ex: Exception) {
            onRecognitionListener.onError("${ex.message}")
            Log.e(TAG_RECOGNITION, "${ex.message}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer.stopListening()
            onRecognitionListener.onEndOfSpeech()  // Call this to handle UI changes
        } catch (ex: Exception) {
            onRecognitionListener.onError("${ex.message}")
            Log.e(TAG_RECOGNITION, "${ex.message}")
        }
    }

    private fun getErrorText(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
    }
}
