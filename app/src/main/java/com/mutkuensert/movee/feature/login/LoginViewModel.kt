package com.mutkuensert.movee.feature.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.domain.login.CompleteAnyUnfinishedLoginTaskUseCase
import com.mutkuensert.movee.domain.login.CompleteLoginUseCase
import com.mutkuensert.movee.domain.login.GetRequestTokenUseCase
import com.mutkuensert.movee.domain.login.LogoutUseCase
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.util.NavConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        savedStateHandle[NavConstants.Login.KEY_IS_REDIRECTED_FROM_TMDB_LOGIN]
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?>
        get() {
            viewModelScope.launch {
                sessionManager.isLoggedIn().collectLatest {
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