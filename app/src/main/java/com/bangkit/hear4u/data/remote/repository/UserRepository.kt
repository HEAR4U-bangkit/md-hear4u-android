package com.bangkit.hear4u.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.local.preferences.UserPreference
import com.bangkit.hear4u.data.remote.api.ApiService
import com.bangkit.hear4u.data.remote.response.LoginResponse
import com.bangkit.hear4u.data.remote.response.RegisterResponse
import com.bangkit.hear4u.di.StateResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor (
    private val userPreferences: UserPreference,
    private val apiService: ApiService
) {
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

    suspend fun login(email: String, password: String): LiveData<StateResult<LoginResponse>> = liveData {
        emit(StateResult.Loading)
        try {
            val response = apiService.login(email, password)
            emit(StateResult.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(StateResult.Error(errorBody.message.toString()))
        }
    }
    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }




    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun clearInstance() {
            instance = null
        }
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }

}