package movee.domain.account

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchAccountDetailsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    suspend fun execute() = accountRepository.fetchUserDetails()
}