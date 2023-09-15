package movee.domain.library

interface UserManager {
    fun getUserId(): Int?
    fun setCurrentUserId(id: Int): Boolean
    fun removeCurrentUser(): Boolean
}