package movee.domain.login

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import movee.domain.library.SessionManager

class CollectLoginEventsUseCase @Inject constructor(private val sessionManager: SessionManager) {
    private var previousLoginEvent: LoginEvent? = null

    fun execute(): Flow<LoginEvent> {
        return sessionManager.isLoggedInFlow().transform {
            val event = if (it) LoginEvent.Login else LoginEvent.Logout

            if (previousLoginEvent != null && previousLoginEvent != event) {
                emit(event)
            }

            previousLoginEvent = event
        }
    }
}