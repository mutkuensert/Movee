package movee.domain.movie

import com.github.michaelbull.result.onFailure
import javax.inject.Inject
import movee.domain.account.AccountRepository

class AddMovieToFavoriteUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(isFavorite: Boolean, movieId: Int) {
        if (isFavorite) {
            accountRepository.insertFavoriteMovieInCache(movieId)

            accountRepository.addMovieToFavorites(
                isFavorite = true,
                movieId = movieId
            ).onFailure {
                accountRepository.deleteFavoriteMovieInCache(movieId)
            }
        } else {
            accountRepository.deleteFavoriteMovieInCache(movieId = movieId)

            accountRepository.addMovieToFavorites(
                isFavorite = false,
                movieId = movieId
            ).onFailure {
                accountRepository.insertFavoriteMovieInCache(movieId = movieId)
            }
        }
    }
}