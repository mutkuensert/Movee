package movee.domain.account

import javax.inject.Inject
import movee.domain.library.UserManager

class GetAccountDetailsUseCase @Inject constructor(private val userManager: UserManager) {

    fun execute(): AccountDetails? = userManager.getUser()
}