package movee.data.person

import com.github.michaelbull.result.Result
import movee.domain.Failure
import movee.domain.person.PersonRepository
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCast
import movee.domain.person.model.PersonTvCast
import javax.inject.Inject
import movee.data.network.toResult
import movee.data.util.getImageUrl

class PersonRepositoryImpl @Inject constructor(
    private val personApi: PersonApi
) : PersonRepository {

    override suspend fun getPersonMovieCasting(personId: Int): Result<List<PersonMovieCast>, Failure> {
        return personApi.getPersonMovieCasting(personId = personId).toResult(mapper = { response ->
            response.cast.map {
                PersonMovieCast(
                    character = it.character,
                    title = it.title,
                    id = it.id,
                    imageUrl = getImageUrl(it.posterPath)
                )
            }
        })
    }

    override suspend fun getPersonTvCasting(personId: Int): Result<List<PersonTvCast>, Failure> {
        return personApi.getPersonTvCasting(personId).toResult(mapper = { response ->
            response.cast.map {
                PersonTvCast(
                    character = it.character,
                    name = it.name,
                    id = it.id,
                    imageUrl = getImageUrl(it.posterPath)
                )
            }
        })
    }

    override suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure> {
        return personApi.getPersonDetails(personId).toResult(mapper = {
            PersonDetails(
                name = it.name,
                biography = it.biography,
                imageUrl = getImageUrl(it.profilePath)
            )
        })
    }
}