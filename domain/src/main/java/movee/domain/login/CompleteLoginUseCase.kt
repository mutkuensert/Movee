package movee.domain.login

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject
import javax.inject.Singleton
import movee.domain.account.FetchAccountDetailsUseCase
import movee.domain.library.SessionManager
import timber.log.Timber

@Singleton
class CompleteLoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val sessionManager: SessionManager,
    private val fetchAccountDetailsUseCase: FetchAccountDetailsUseCase,
) {

    suspend fun execute(): Boolean {
        val requestToken = sessionManager.getRequestToken() ?: return false

        authenticationRepository.fetchSessionIdWithValidatedRequestToken(requestToken)
            .onFailure {
                sessionManager.removeRequestToken()

                Timber.d(it.message)

                return false
            }
            .onSuccess { sessionId ->
                sessionManager.removeRequestToken()
                sessionManager.insertSessionId(sessionId)
            }

        fetchAccountDetailsUseCase.execute()

        return true
    }
}