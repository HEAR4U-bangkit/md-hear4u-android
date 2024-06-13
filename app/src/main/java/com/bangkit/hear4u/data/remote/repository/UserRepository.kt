package com.bangkit.hear4u.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.local.preferences.UserPreferences
import com.bangkit.hear4u.data.remote.api.ApiService
import com.bangkit.hear4u.data.remote.response.DataItem
import com.bangkit.hear4u.data.remote.response.DetailArticleResponse
import com.bangkit.hear4u.data.remote.response.ErrorResponse
import com.bangkit.hear4u.data.remote.response.LoginResponse
import com.bangkit.hear4u.data.remote.response.RegisterResponse
import com.bangkit.hear4u.data.remote.response.UpdatePasswordResponse
import com.bangkit.hear4u.data.remote.response.UpdateProfileResponse
import com.bangkit.hear4u.di.StateResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
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

    fun login(email: String, password: String): LiveData<StateResult<LoginResponse>> = liveData {
        emit(StateResult.Loading)
        try {
            val response = apiService.login(email, password)
            emit(StateResult.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(StateResult.Error(errorBody.message))
        }
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun getArticle(): LiveData<StateResult<List<DataItem>>> = liveData {
        emit(StateResult.Loading)
        try {
            val response = apiService.getArticle()
            emit(StateResult.Success(response.data))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("UserRepository", "HttpException: ${e.message()}")
            Log.e("UserRepository", "Error Body: $errorBody")
            emit(StateResult.Error(errorResponse.message ?: "Unknown HTTP error"))
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            emit(StateResult.Error(e.message ?: "Unknown error"))
        }
    }

    fun getArticleById(id: String): LiveData<StateResult<DetailArticleResponse>> = liveData {
        emit(StateResult.Loading)
        try {
            val response = apiService.getDetailArticles(id)
            emit(StateResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailArticleResponse::class.java)
            emit(StateResult.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(StateResult.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }


    fun changePassword(
        id: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): LiveData<StateResult<UpdatePasswordResponse>> = liveData {
        emit(StateResult.Loading)
        try {
            val responseSuccess = apiService.changePassword(id, oldPassword, newPassword, confirmPassword)
            emit(StateResult.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val responseError = Gson().fromJson(errorBody, UpdatePasswordResponse::class.java)
            emit(StateResult.Error(responseError.message.toString()))
        }
    }

    fun editProfile(
        id: String,
        email: String,
        fullname: String
    ): LiveData<StateResult<UpdateProfileResponse>> = liveData {
        emit(StateResult.Loading)
        try {
            val responseSuccess = apiService.editProfile(id, fullname, email)
            emit(StateResult.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val responseError = Gson().fromJson(errorBody, UpdateProfileResponse::class.java)
            emit(StateResult.Error(responseError.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun clearInstance() {
            INSTANCE = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService, userPreferences)
            }.also { INSTANCE = it }
    }

}