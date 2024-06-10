package com.bangkit.hear4u.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.hear4u.R
import com.bangkit.hear4u.data.adapter.ArticleAdapter
import com.bangkit.hear4u.databinding.ActivityMainBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bangkit.hear4u.ui.landingPage.LandingActivity
import com.bangkit.hear4u.ui.landingPage.WelcomeFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()
        setupView()
        setupClickListeners()
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

        }
    }
}