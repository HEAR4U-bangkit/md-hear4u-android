package com.bangkit.hear4u.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun changePassword(
        id: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
    ) = repository.changePassword(id, oldPassword, newPassword, confirmPassword)


    fun editProfile(id: String, email: String, fullname: String) =
        repository.editProfile(id, email, fullname)


}