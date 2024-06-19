package com.bangkit.hear4u.ui.speechToText


interface onRecognitionListener {
    fun onReadyForSpeech()
    fun onBeginningOfSpeech()
    fun onEndOfSpeech()
    fun onError(error: String)
    fun onResults(results: String)
}