package com.bangkit.hear4u.ui.register

import androidx.lifecycle.ViewModel
import com.bangkit.hear4u.data.remote.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(
        fullname: String,
        email: String,
        password: String,
    ) = repository.userRegister(fullname, email, password)
}