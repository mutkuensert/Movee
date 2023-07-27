package com.mutkuensert.movee.library.user

import android.annotation.SuppressLint
import android.content.Context
import com.mutkuensert.movee.library.security.EncryptedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val PREFS_USER = "userPreferences"
private const val KEY_USER = "userKey"

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_USER, context)
    fun getCurrentUser(): UserDetails? {
        return try {
            encryptedSharedPreferences.getString(KEY_USER, null)
                ?.let { json.decodeFromString<UserDetails>(it) }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    @SuppressLint("ApplySharedPref")
    fun setCurrentUser(user: UserDetails): Boolean {
        return try {
            val userJsonStr = json.encodeToString(user)

            encryptedSharedPreferences.edit()
                .putString(KEY_USER, userJsonStr)
                .commit()
        } catch (e: Throwable) {
            Timber.e(e)
            false
        }
    }

    fun removeCurrentUser(): Boolean {
        return encryptedSharedPreferences.edit().remove(KEY_USER).commit()
    }
}

@Serializable
data class UserDetails(
    val avatarPath: String?,
    val gravatarHash: String?,
    val id: Int,
    val includeAdult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)