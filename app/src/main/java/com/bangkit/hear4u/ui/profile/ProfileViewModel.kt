package com.bangkit.hear4u.ui.profile

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.hear4u.data.remote.repository.UserRepository
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bangkit.hear4u.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: UserRepository) : ViewModel() {
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}