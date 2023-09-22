package movee.domain.tvshow

import com.github.michaelbull.result.onFailure
import javax.inject.Inject
import movee.domain.account.AccountRepository

class AddTvShowToFavoriteUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(isFavorite: Boolean, tvShowId: Int) {
        if (isFavorite) {
            accountRepository.insertFavoriteTvShowInCache(tvShowId)

            accountRepository.addTvShowToFavorites(
                isFavorite = true,
                tvShowId = tvShowId
            ).onFailure {
                accountRepository.deleteFavoriteTvShowInCache(tvShowId)
            }
        } else {
            accountRepository.deleteFavoriteTvShowInCache(tvShowId = tvShowId)

            accountRepository.addTvShowToFavorites(
                isFavorite = false,
                tvShowId = tvShowId
            ).onFailure {
                accountRepository.insertFavoriteTvShowInCache(tvShowId = tvShowId)
            }
        }
    }
}