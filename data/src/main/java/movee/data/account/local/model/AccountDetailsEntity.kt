package movee.data.account.local.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsEntity(
    val profilePicturePath: String?,
    val id: Int,
    val includeAdult: Boolean,
    val name: String,
    val userName: String
)
