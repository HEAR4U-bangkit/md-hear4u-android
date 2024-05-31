package com.bangkit.hear4u.ui.login

import androidx.lifecycle.ViewModel
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.remote.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    suspend fun login(email: String, password: String) = repository.login(email, password)
    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }
}