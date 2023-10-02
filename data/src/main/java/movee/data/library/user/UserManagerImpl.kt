package movee.data.library.user

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import movee.data.account.local.model.AccountDetailsEntity
import movee.data.library.EncryptedPreferences
import movee.data.util.getImageUrl
import movee.domain.account.AccountDetails
import movee.domain.library.UserManager

private const val PREFS_USER = "userPreferences"
private const val KEY_USER_DETAILS = "userDetails"

class UserManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
) : UserManager {
    private val encryptedSharedPreferences = EncryptedPreferences.create(PREFS_USER, context)
    override fun getUser(): AccountDetails? {
        val userDetailsJsonString =
            encryptedSharedPreferences.getString(KEY_USER_DETAILS, null)
                ?: return null

        val userDetailsEntity = json.decodeFromString<AccountDetailsEntity>(userDetailsJsonString)

        return AccountDetails(
            profilePictureUrl = getImageUrl(userDetailsEntity.profilePicturePath),
            id = userDetailsEntity.id,
            userName = userDetailsEntity.userName
        )
    }

    @SuppressLint("ApplySharedPref")
    override fun setCurrentUser(
        profilePicturePath: String?,
        id: Int,
        includeAdult: Boolean,
        name: String,
        userName: String
    ): Boolean {
        return encryptedSharedPreferences.edit()
            .putString(
                KEY_USER_DETAILS, json.encodeToString(
                    AccountDetailsEntity(
                        profilePicturePath = profilePicturePath,
                        id = id,
                        includeAdult = includeAdult,
                        name = name,
                        userName = userName
                    )
                )
            )
            .commit()
    }

    override fun removeCurrentUser(): Boolean {
        return encryptedSharedPreferences.edit().remove(KEY_USER_DETAILS).commit()
    }
}