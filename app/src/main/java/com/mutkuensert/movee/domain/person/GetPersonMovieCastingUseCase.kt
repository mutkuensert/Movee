package com.mutkuensert.movee.domain.person

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPersonMovieCastingUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {

    suspend fun execute(personId: Int): Flow<Phase<List<PersonMovieCast>>> {
        return GetPhaseFlow<List<PersonMovieCast>>().execute {
            personRepository.getPersonMovieCasting(personId)
        }
    }
}