package movee.domain.library

import movee.domain.account.AccountDetails

interface UserManager {
    fun getUser(): AccountDetails?
    fun setCurrentUser(
        profilePicturePath: String?,
        id: Int,
        includeAdult: Boolean,
        name: String,
        userName: String
    ): Boolean

    fun removeCurrentUser(): Boolean
}