package com.bangkit.hear4u.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.hear4u.data.local.preferences.UserPreference
import com.bangkit.hear4u.data.local.preferences.dataStore
import com.bangkit.hear4u.databinding.ActivitySplashScreenBinding
import com.bangkit.hear4u.ui.landingPage.LandingActivity
import com.bangkit.hear4u.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

 
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        userPreference = UserPreference.getInstance(dataStore)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.isLogin) {
                navigateToMainActivity()
            } else {
                navigateToLandingActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SplashScreen, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun navigateToLandingActivity() {
        val intent = Intent(this@SplashScreen, LandingActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
