package com.bangkit.hear4u.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.hear4u.R
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.local.preferences.UserPreference
import com.bangkit.hear4u.data.local.preferences.dataStore
import com.bangkit.hear4u.data.remote.response.LoginResponse
import com.bangkit.hear4u.databinding.ActivityLoginBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bangkit.hear4u.ui.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(dataStore)

        showLoading(false)
        setupAction()
    }



    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            try {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                if (email.isEmpty()) {
                    binding.edLoginEmail.error = getString(R.string.fill_email)
                } else if (password.isEmpty()) {
                    binding.edLoginPassword.error = getString(R.string.fill_password)
                } else {
                    lifecycleScope.launch {
                        viewModel.login(email, password).observe(this@LoginActivity) { result ->
                            if (result != null) {
                                when (result) {
                                    is StateResult.Loading -> {
                                        showLoading(true)
                                    }

                                    is StateResult.Success -> {
                                        showLoading(false)
                                        showToast("Login berhasil!")
                                        lifecycleScope.launch {
                                            val loginData = result.data
                                            if (loginData != null && loginData.data != null && loginData.data.user != null) {
                                                saveSession(
                                                    UserModel(
                                                        loginData.data?.token.toString(),
                                                        loginData.data?.user?.fullname.toString(),
                                                        email,
                                                        true
                                                    )
                                                )
                                            } else {
                                                showToast("Data login tidak lengkap.")
                                            }
                                        }
                                    }

                                    is StateResult.Error -> {
                                        showLoading(false)
                                        showToast(result.error)
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (e: HttpException) {
                showLoading(false)
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                showToast(errorResponse.message)
            }
        }
    }

    private fun saveSession(session: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
