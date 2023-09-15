package movee.data.library.user

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import movee.data.library.EncryptedPreferences
import movee.domain.library.UserManager

private const val PREFS_USER = "userPreferences"
private const val KEY_USER_ID = "userIdKey"

@Singleton
class UserManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserManager {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_USER, context)
    override fun getUserId(): Int? {
        val userId = encryptedSharedPreferences.getInt(KEY_USER_ID, -1)

        return if (userId == -1) null else userId
    }

    @SuppressLint("ApplySharedPref")
    override fun setCurrentUserId(id: Int): Boolean {
        return encryptedSharedPreferences.edit()
            .putInt(KEY_USER_ID, id)
            .commit()
    }

    override fun removeCurrentUser(): Boolean {
        return encryptedSharedPreferences.edit().remove(KEY_USER_ID).commit()
    }
}