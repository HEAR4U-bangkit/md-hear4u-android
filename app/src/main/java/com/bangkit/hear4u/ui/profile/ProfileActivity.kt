package com.bangkit.hear4u.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.ActivityProfileBinding
import com.bangkit.hear4u.ui.landingPage.LandingFragment

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.profile_container, profileFragment)
            .addToBackStack(null)
            .commit()

        binding.toolbar.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }
}