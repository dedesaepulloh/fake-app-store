package com.dedesaepulloh.fakeappstore.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    private val dataStore = context.dataStore

    companion object {
        val USER_KEY = stringPreferencesKey(name = "user_key")
        val IS_LOGIN_KEY = booleanPreferencesKey(name = "is_login_key")
    }

    suspend fun save(user: User) {
        val jsonUser = gson.toJson(user, User::class.java)
        dataStore.edit { pref ->
            pref[USER_KEY] = jsonUser
        }
    }

    suspend fun setLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { pref ->
            pref[IS_LOGIN_KEY] = isLoggedIn
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[IS_LOGIN_KEY] ?: false
        }
    }

    fun readUser(): Flow<User?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
            .map { preference ->
                val user = preference[USER_KEY]
                if (user.isNullOrEmpty()) {
                    null
                } else {
                    gson.fromJson(user, User::class.java)
                }
            }
    }

    suspend fun remove(){
        dataStore.edit { pref->
            pref.clear()
        }
    }

}