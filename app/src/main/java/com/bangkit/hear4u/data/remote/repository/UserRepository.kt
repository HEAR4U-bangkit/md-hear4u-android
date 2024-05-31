package com.bangkit.hear4u.data.remote.repository

import androidx.lifecycle.liveData
import com.bangkit.hear4u.data.local.preferences.UserPreference
import com.bangkit.hear4u.data.remote.api.ApiService
import com.bangkit.hear4u.data.remote.response.RegisterResponse
import com.bangkit.hear4u.di.StateResult
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository private constructor (private val userPreferences: UserPreference, private val apiService: ApiService) {
    fun userRegister(fullname: String, email: String, password: String) = liveData {
        emit(StateResult.Loading)
        try {
            val responseSuccess = apiService.register(fullname, email, password)
            emit(StateResult.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val responseError = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(responseError.message.let { StateResult.Error(it) })
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }

}