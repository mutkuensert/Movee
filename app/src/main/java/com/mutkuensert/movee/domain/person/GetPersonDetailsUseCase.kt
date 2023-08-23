package com.mutkuensert.movee.domain.person

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.person.model.PersonDetails
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPersonDetailsUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {

    suspend fun execute(personId: Int): Flow<Phase<PersonDetails>> {
        return GetPhaseFlow.execute {
            personRepository.getPersonDetails(personId)
        }
    }
}