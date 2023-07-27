package com.mutkuensert.movee.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.domain.login.LoginUseCase
import com.mutkuensert.movee.domain.login.LogoutUseCase
import com.mutkuensert.movee.library.session.SessionManager
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
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {
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

    fun login(user: String, password: String) {
        viewModelScope.launch {
            val isLoginSuccessful = loginUseCase.execute(user, password)

            if (isLoginSuccessful) {
                _loginEvent.emit(LoginEvent.Login)
            }
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