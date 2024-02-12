package movee.presentation.core

sealed interface UiState<T> {
    class Loading<T> : UiState<T>

    class Empty<T> : UiState<T>

    class Success<T>(val data: T) : UiState<T>
}