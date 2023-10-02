package movee.presentation.login.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.account.GetAccountDetailsUseCase
import movee.domain.login.CollectLoginEventsUseCase
import movee.domain.login.LogoutUseCase
import movee.presentation.login.navigation.ROUTE_LOGIN
import movee.presentation.navigation.navigator.MainNavigator
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val mainNavigator: MainNavigator,
    private val collectLoginEventsUseCase: CollectLoginEventsUseCase,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(ProfileUiModel.empty())
    val uiModel = _uiModel.asStateFlow()

    val loginEvent = collectLoginEventsUseCase.execute()

    fun getAccountDetails() {
        viewModelScope.launch {
            _uiModel.value = getAccountDetailsUseCase.execute().run {
                Timber.e(this.toString())
                ProfileUiModel(
                    userName = this?.userName ?: "",
                    profilePictureUrl = this?.profilePictureUrl
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            if (logoutUseCase.execute()) mainNavigator.navigateUp(ROUTE_LOGIN)
        }
    }

    fun onBackPressed() {
        mainNavigator.navigateUp(popUpTo = ROUTE_LOGIN, isInclusive = true)
    }
}

data class ProfileUiModel(val userName: String, val profilePictureUrl: String?) {

    companion object {
        fun empty(): ProfileUiModel {
            return ProfileUiModel(userName = "", profilePictureUrl = null)
        }
    }
}