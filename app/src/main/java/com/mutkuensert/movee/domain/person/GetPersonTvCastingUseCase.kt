package com.mutkuensert.movee.domain.person

import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.core.GetPhaseFlow
import com.mutkuensert.movee.domain.person.model.PersonTvCast
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPersonTvCastingUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {

    suspend fun execute(personId: Int): Flow<Phase<List<PersonTvCast>>> {
        return GetPhaseFlow.execute {
            personRepository.getPersonTvCasting(personId)
        }
    }
}