package com.bangkit.hear4u.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.remote.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


}