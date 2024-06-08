package com.bangkit.hear4u.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
            preferences[FULLNAME_KEY] = user.fullname
            preferences[IS_LOGIN_KEY] = true
        }
        // Logging for debugging
        println("User session saved: email=${user.email}, fullname=${user.fullname}, token=${user.token}")
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[FULLNAME_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            ).also { userModel ->
                // Logging for debugging
                println("User session retrieved: email=${userModel.email}, fullname=${userModel.fullname}, token=${userModel.token}")
            }
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        // Logging for debugging
        println("User session cleared.")
    }

    companion object {

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val FULLNAME_KEY = stringPreferencesKey("fullname")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        private var INSTANCE: UserPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}