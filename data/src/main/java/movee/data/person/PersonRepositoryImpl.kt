package movee.data.person

import com.github.michaelbull.result.Result
import movee.domain.Failure
import movee.domain.person.PersonRepository
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCasting
import movee.domain.person.model.PersonTvCasting
import javax.inject.Inject
import movee.data.network.toResult
import movee.data.util.getImageUrl

class PersonRepositoryImpl @Inject constructor(
    private val personApi: PersonApi
) : PersonRepository {

    override suspend fun getPersonMovieCasting(personId: Int): Result<List<PersonMovieCasting>, Failure> {
        return personApi.getPersonMovieCasting(personId = personId).toResult(mapper = { response ->
            response.cast.map {
                PersonMovieCasting(
                    character = it.character,
                    title = it.title,
                    id = it.id,
                    imageUrl = getImageUrl(it.posterPath)
                )
            }
        })
    }

    override suspend fun getPersonTvCasting(personId: Int): Result<List<PersonTvCasting>, Failure> {
        return personApi.getPersonTvCasting(personId).toResult(mapper = { response ->
            response.cast.map {
                PersonTvCasting(
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