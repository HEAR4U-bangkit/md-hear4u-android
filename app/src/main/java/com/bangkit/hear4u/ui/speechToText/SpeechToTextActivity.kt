package com.bangkit.hear4u.ui.speechToText

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.hear4u.databinding.ActivitySpeechToTextBinding

class SpeechToTextActivity : AppCompatActivity(), onRecognitionListener  {
    private lateinit var binding: ActivitySpeechToTextBinding
    private lateinit var speechToTextConverter: SpeechToTextConverter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeechToTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        speechToTextConverter = SpeechToTextConverter(this, this)

        // Request microphone permission if not granted already
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        setupClickListener()
    }

    private fun setupClickListener(){
        binding.toolbarSpeech.setNavigationOnClickListener {
            finish()
            onBackPressed()
        }

        binding.btnSpeak.setOnClickListener {
            // Start listening with Indonesian language
            speechToTextConverter.startListening("id-ID")
        }

        binding.lavMicAnimation.setOnClickListener {
            // Stop listening when animation is clicked
            speechToTextConverter.stopListening()
            binding.lavMicAnimation.pauseAnimation()  // Pause the animation
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Stop listening
        speechToTextConverter.stopListening()
    }
    override fun onReadyForSpeech() {
        binding.btnSpeak.visibility = View.GONE
        binding.tvResult.visibility = View.GONE
        binding.lavMicAnimation.visibility = View.VISIBLE
        binding.loading.visibility =View.VISIBLE
        binding.logo.visibility=View.VISIBLE
        binding.listen.visibility = View.VISIBLE
    }

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {
        binding.lavMicAnimation.visibility = View.GONE
        binding.lavMicAnimation.cancelAnimation()  // Ensure animation is stopped
        binding.btnSpeak.visibility = View.VISIBLE
        binding.tvResult.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.logo.visibility = View.GONE
        binding.listen.visibility = View.GONE
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        binding.btnSpeak.visibility = View.VISIBLE
        binding.tvResult.visibility = View.VISIBLE
        binding.tvResult.text = "Sorry, Please try again"
        binding.lavMicAnimation.visibility = View.GONE
        binding.loading.visibility = View.GONE
        binding.listen.visibility = View.GONE
        binding.logo.visibility=View.GONE
    }

    override fun onResults(results: String) {
        binding.btnSpeak.visibility = View.VISIBLE
        binding.tvResult.visibility = View.VISIBLE
        binding.lavMicAnimation.visibility = View.GONE
        binding.loading.visibility=View.GONE
        binding.logo.visibility=View.GONE
        binding.listen.visibility = View.GONE
        binding.tvResult.text = results
    }

}