package movee.domain.person

import com.github.michaelbull.result.Result
import movee.domain.Failure
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCast
import movee.domain.person.model.PersonTvCast

interface PersonRepository {
    suspend fun getPersonMovieCasting(personId: Int): Result<List<PersonMovieCast>, Failure>
    suspend fun getPersonTvCasting(personId: Int): Result<List<PersonTvCast>, Failure>
    suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure>
}