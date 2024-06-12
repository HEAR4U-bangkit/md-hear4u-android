package com.bangkit.hear4u.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.hear4u.databinding.ActivityRegisterBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSubmitRegister.setOnClickListener {
            val fullname = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                setupAction(fullname,email, password)
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupAction(fullname: String, email: String, password: String) {
        viewModel.register(fullname, email, password).observe(this) { result ->
            if (result != null){
                when (result){
                    is StateResult.Loading ->{
                        loading(true)
                    }
                    is StateResult.Success ->{
                        result.data.message.let { toast(it) }
                        Log.d("Berhasil bikin akun", result.data.message)
                        loading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Akunnya sudah jadi nih.")
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                    }
                    is StateResult.Error ->{
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        loading(false)
                    }
                }
            }
        }
    }
    private fun loading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}