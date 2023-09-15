package movee.domain.person

import com.mutkuensert.androidphase.Phase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import movee.domain.person.model.PersonDetails
import movee.domain.util.GetPhaseFlow

class GetPersonDetailsUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {

    suspend fun execute(personId: Int): Flow<Phase<PersonDetails>> {
        return GetPhaseFlow.execute {
            personRepository.getPersonDetails(personId)
        }
    }
}