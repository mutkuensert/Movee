package movee.domain.library

import kotlinx.coroutines.flow.Flow

interface SessionManager {

    fun isLoggedInFlow(): Flow<Boolean>
    fun getSessionId(): String?
    fun removeSession()
    fun isLoggedIn(): Boolean
    fun getRequestToken(): String?
    fun insertRequestToken(token: String)
    fun removeRequestToken()
    fun insertSessionId(id: String)
}