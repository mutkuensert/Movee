package movee.domain.login

import com.github.michaelbull.result.getOrElse
import javax.inject.Inject
import javax.inject.Singleton
import movee.domain.library.SessionManager

@Singleton
class GetRequestTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val sessionManager: SessionManager,
) {

    suspend fun execute(): String? {
        val requestToken = authenticationRepository.fetchRequestToken().getOrElse { return null }
        sessionManager.insertRequestToken(requestToken)

        return requestToken
    }
}