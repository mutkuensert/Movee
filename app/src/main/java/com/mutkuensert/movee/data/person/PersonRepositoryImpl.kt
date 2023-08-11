package com.mutkuensert.movee.data.person

import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.person.PersonRepository
import com.mutkuensert.movee.domain.person.model.PersonDetails
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import com.mutkuensert.movee.domain.person.model.PersonTvCast
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personApi: PersonApi
) : PersonRepository {

    override suspend fun getPersonMovieCast(personId: Int): Result<List<PersonMovieCast>, Failure> {
        return personApi.getPersonMovieCredits(personId = personId).toResult(mapper = { response ->
            response.cast.map {
                PersonMovieCast(
                    character = it.character,
                    title = it.title,
                    id = it.id,
                    posterPath = it.posterPath
                )
            }
        })
    }

    override suspend fun getPersonTvCast(personId: Int): Result<List<PersonTvCast>, Failure> {
        return personApi.getPersonTvCredits(personId).toResult(mapper = { response ->
            response.cast.map {
                PersonTvCast(
                    character = it.character,
                    name = it.name,
                    id = it.id,
                    posterPath = it.posterPath
                )
            }
        })
    }

    override suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure> {
        return personApi.getPersonDetails(personId).toResult(mapper = {
            PersonDetails(name = it.name, biography = it.biography, profilePath = it.profilePath)
        })
    }
}