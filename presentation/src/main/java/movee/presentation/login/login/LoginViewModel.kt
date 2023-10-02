package movee.presentation.login.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.library.SessionManager
import movee.domain.login.CollectLoginEventsUseCase
import movee.domain.login.CompleteAnyUnfinishedLoginTaskUseCase
import movee.domain.login.CompleteLoginUseCase
import movee.domain.login.GetRequestTokenUseCase
import movee.presentation.login.navigation.KEY_IS_REDIRECTED_FROM_TMDB_LOGIN
import movee.presentation.navigation.navigator.LoginNavigator


const val APP_DEEP_LINK = "mutkuensert.movee://app/"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getRequestTokenUseCase: GetRequestTokenUseCase,
    private val sessionManager: SessionManager,
    private val completeLoginUseCase: CompleteLoginUseCase,
    private val completeAnyUnfinishedLoginTaskUseCase: CompleteAnyUnfinishedLoginTaskUseCase,
    private val collectLoginEventsUseCase: CollectLoginEventsUseCase,
    private val loginNavigator: LoginNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var redirectedFromTmdbLogin: String? =
        savedStateHandle[KEY_IS_REDIRECTED_FROM_TMDB_LOGIN]

    val loginEvent = collectLoginEventsUseCase.execute()

    private val _requestToken = MutableStateFlow<String?>(null)
    val requestToken = _requestToken.asStateFlow()

    init {
        if(sessionManager.isLoggedIn()) loginNavigator.navigateToProfileScreen()

        if (redirectedFromTmdbLogin != null && !sessionManager.isLoggedIn()) {
            viewModelScope.launch {
                if (completeLoginUseCase.execute()) loginNavigator.navigateToProfileScreen()
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
}
