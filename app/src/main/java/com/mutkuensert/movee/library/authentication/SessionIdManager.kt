package com.mutkuensert.movee.library.authentication

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

private const val PREFS_SESSION: String = "session"
private const val KEY_SESSION_ID: String = "sessionId"

class SessionIdManager(private val context: Context) {
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        PREFS_SESSION,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun insertSessionId(id: String) {
        encryptedSharedPreferences.edit {
            putString(KEY_SESSION_ID, id)
        }
    }

    fun removeSessionId() {
        encryptedSharedPreferences.edit {
            remove(KEY_SESSION_ID)
        }
    }

    fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(KEY_SESSION_ID, null)
    }
}