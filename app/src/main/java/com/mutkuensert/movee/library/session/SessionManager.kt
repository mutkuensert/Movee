package com.mutkuensert.movee.library.session

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

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_SESSION, context)
    private val _isLoggedIn = MutableStateFlow<Boolean>(
        encryptedSharedPreferences.contains(KEY_SESSION_ID)
    )

    fun insertSessionId(id: String) {
        val isInserted = encryptedSharedPreferences.edit().putString(KEY_SESSION_ID, id).commit()
        _isLoggedIn.value = isInserted
    }

    fun removeSession() {
        val isRemoved = encryptedSharedPreferences.edit().remove(KEY_SESSION_ID).commit()
        _isLoggedIn.value = !isRemoved
    }

    fun isLoggedIn(): Flow<Boolean> {
        return _isLoggedIn.asStateFlow()
    }

    fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(KEY_SESSION_ID, null)
    }
}