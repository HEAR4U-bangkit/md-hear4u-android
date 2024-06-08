package com.bangkit.hear4u.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.hear4u.data.local.preferences.UserPreferences
import com.bangkit.hear4u.data.local.preferences.dataStore
import com.bangkit.hear4u.databinding.ActivitySplashScreenBinding
import com.bangkit.hear4u.ui.landingPage.LandingActivity
import com.bangkit.hear4u.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Handler().postDelayed({
        lifecycleScope.launch {
            val userPreferences = UserPreferences.getInstance(dataStore)
            val user = userPreferences.getSession().first()
            val intent = if (user.isLogin) {
                Intent(this@SplashScreen, MainActivity::class.java)
            } else {
                Intent(this@SplashScreen, LandingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
