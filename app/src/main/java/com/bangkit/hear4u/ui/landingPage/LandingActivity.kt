package com.bangkit.hear4u.ui.landingPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.ActivityLandingBinding
import com.bangkit.hear4u.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LandingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val welcomeFragment = LandingFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, welcomeFragment)
            .commit()
    }
}