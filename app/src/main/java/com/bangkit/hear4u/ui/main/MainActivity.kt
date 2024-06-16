package com.bangkit.hear4u.ui.main

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.hear4u.R
import com.bangkit.hear4u.data.adapter.ArticleAdapter
import com.bangkit.hear4u.databinding.ActivityMainBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.services.AudioRecordService
import com.bangkit.hear4u.services.SocketManager
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bangkit.hear4u.ui.landingPage.LandingActivity
import com.bangkit.hear4u.ui.profile.ProfileActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private var permissionToRecordAccepted = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var serverResponseReceiver: BroadcastReceiver
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getSession()
        setupView()
        setupClickListeners()

        val serviceIntent = Intent(this, AudioRecordService::class.java)
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                startService(serviceIntent)
            }else{
                stopService(serviceIntent)
            }
        }
        serverResponseReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent) {
                val response = intent.getStringExtra("response")
                try {
                    val jsonResponse = JSONObject(response)
                    val label = jsonResponse.getString("label")
                    val confidence = jsonResponse.getInt("confidence")
                    binding.tvOutput.text = "Label: $label, Confidence: $confidence%"
                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.tvOutput.text = "Failed to parse server response"
                }
            }
        }

//        val intentFilter = IntentFilter("com.example.realtimesound.SERVER_RESPONSE")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            registerReceiver(serverResponseReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
//        } else {
//            registerReceiver(serverResponseReceiver, intentFilter)
//        }

        val intentFilter = IntentFilter("com.example.realtime sound.SERVER_RESPONSE")
        registerReceiver(serverResponseReceiver, intentFilter)

        requestPermissions()
        SocketManager.connect()
    }

    //Request Permission Audio
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.helloHome.text = getString(R.string.hello_user_placeholder, user.fullname)
                setupAction()
            }
        }
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

    }

    private fun setupAction() {
        lifecycleScope.launch {
            viewModel.getArticle().observe(this@MainActivity) { article ->
                when (article) {
                    is StateResult.Error -> {
                        binding.progressBar3.visibility = View.INVISIBLE
                        val error = article.error
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()

                    }

                    is StateResult.Loading -> {
                        binding.progressBar3.visibility = View.VISIBLE

                    }

                    is StateResult.Success -> {
                        binding.progressBar3.visibility = View.INVISIBLE
                        val adapter = ArticleAdapter()
                        adapter.submitList(article.data)
                        binding.recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.profil.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        SocketManager.disconnect()
        unregisterReceiver(serverResponseReceiver)
    }
}