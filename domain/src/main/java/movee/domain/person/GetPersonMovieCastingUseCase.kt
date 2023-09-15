package movee.domain.person

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.person.model.PersonMovieCast
import movee.domain.util.GetPhaseFlow

class GetPersonMovieCastingUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {

    suspend fun execute(personId: Int): Flow<Phase<List<PersonMovieCast>>> {
        return GetPhaseFlow.execute {
            personRepository.getPersonMovieCasting(personId)
        }
    }
}