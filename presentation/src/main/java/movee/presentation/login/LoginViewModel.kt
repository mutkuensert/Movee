package movee.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import movee.domain.library.SessionManager
import movee.domain.login.CompleteAnyUnfinishedLoginTaskUseCase
import movee.domain.login.CompleteLoginUseCase
import movee.domain.login.GetRequestTokenUseCase
import movee.domain.login.LogoutUseCase
import movee.presentation.login.navigation.KEY_IS_REDIRECTED_FROM_TMDB_LOGIN


const val APP_DEEP_LINK = "mutkuensert.movee://app/"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getRequestTokenUseCase: GetRequestTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager,
    private val completeLoginUseCase: CompleteLoginUseCase,
    private val completeAnyUnfinishedLoginTaskUseCase: CompleteAnyUnfinishedLoginTaskUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var redirectedFromTmdbLogin: String? =
        savedStateHandle[KEY_IS_REDIRECTED_FROM_TMDB_LOGIN]
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?>
        get() {
            viewModelScope.launch {
                sessionManager.isLoggedInFlow().collectLatest {
                    _isLoggedIn.value = it
                }
            }
            return _isLoggedIn.asStateFlow()
        }

    private val _loginEvent = MutableSharedFlow<LoginEvent?>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _requestToken = MutableStateFlow<String?>(null)
    val requestToken = _requestToken.asStateFlow()

    init {
        if (redirectedFromTmdbLogin != null && isLoggedIn.value != true) {
            viewModelScope.launch {
                val isLoggedIn = completeLoginUseCase.execute()

                if (isLoggedIn) _loginEvent.emit(LoginEvent.Login)
            }
        } else {
            viewModelScope.launch { completeAnyUnfinishedLoginTaskUseCase.execute() }
        }
    }

    fun login() {
        viewModelScope.launch {
            _requestToken.value = getRequestTokenUseCase.execute()
        }
    }

    fun logout() {
        viewModelScope.launch {
            val isLogoutSuccessful = logoutUseCase.execute()

            if (isLogoutSuccessful) {
                _loginEvent.emit(LoginEvent.Logout)
            }
        }
    }
}

enum class LoginEvent {
    Login, Logout
}