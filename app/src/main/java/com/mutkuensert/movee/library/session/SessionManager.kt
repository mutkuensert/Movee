package com.mutkuensert.movee.library.session

import android.annotation.SuppressLint
import android.content.Context
import com.mutkuensert.movee.library.security.EncryptedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_SESSION: String = "sessionPreferences"
private const val KEY_SESSION_ID: String = "sessionIdKey"
private const val KEY_REQUEST_TOKEN: String = "requestTokenKey"

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_SESSION, context)
    private val _isLoggedIn = MutableStateFlow<Boolean>(
        encryptedSharedPreferences.contains(KEY_SESSION_ID)
    )

    @SuppressLint("ApplySharedPref")
    fun insertRequestToken(token: String) {
        encryptedSharedPreferences.edit().putString(KEY_REQUEST_TOKEN, token).commit()
    }

    fun getRequestToken(): String? {
        return encryptedSharedPreferences.getString(KEY_REQUEST_TOKEN, null)
    }

    @SuppressLint("ApplySharedPref")
    fun removeRequestToken() {
        encryptedSharedPreferences.edit().remove(KEY_REQUEST_TOKEN).commit()
    }

    fun insertSessionId(id: String) {
        val isInserted = encryptedSharedPreferences.edit().putString(KEY_SESSION_ID, id).commit()
        _isLoggedIn.value = isInserted
    }

    fun removeSession() {
        val isRemoved = encryptedSharedPreferences.edit().remove(KEY_SESSION_ID).commit()
        _isLoggedIn.value = !isRemoved
    }

    fun isLoggedInFlow(): Flow<Boolean> {
        return _isLoggedIn.asStateFlow()
    }

    fun isLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

    fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(KEY_SESSION_ID, null)
    }
}