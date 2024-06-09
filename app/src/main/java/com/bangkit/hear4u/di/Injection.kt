

import android.content.Context
import com.bangkit.hear4u.data.local.preferences.UserPreferences
import com.bangkit.hear4u.data.local.preferences.dataStore
import com.bangkit.hear4u.data.remote.api.ApiConfig
import com.bangkit.hear4u.data.remote.repository.UserRepository
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        // Logging for debugging
        println("Token used for ApiService: ${user.token}")
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}