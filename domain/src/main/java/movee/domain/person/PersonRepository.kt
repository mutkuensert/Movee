package movee.domain.person

import com.github.michaelbull.result.Result
import movee.domain.Failure
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCasting
import movee.domain.person.model.PersonTvCasting

interface PersonRepository {
    suspend fun getPersonMovieCasting(personId: Int): Result<List<PersonMovieCasting>, Failure>
    suspend fun getPersonTvCasting(personId: Int): Result<List<PersonTvCasting>, Failure>
    suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure>
}