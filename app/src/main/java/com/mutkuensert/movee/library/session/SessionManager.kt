package com.mutkuensert.movee.library.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_SESSION: String = "session"
private const val KEY_SESSION_ID: String = "sessionId"

@Singleton
class SessionManager(private val context: Context) {
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        PREFS_SESSION,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
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