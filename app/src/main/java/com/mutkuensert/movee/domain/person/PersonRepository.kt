package com.mutkuensert.movee.domain.person

import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.person.model.PersonDetails
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import com.mutkuensert.movee.domain.person.model.PersonTvCast

interface PersonRepository {
    suspend fun getPersonMovieCasting(personId: Int): Result<List<PersonMovieCast>, Failure>
    suspend fun getPersonTvCasting(personId: Int): Result<List<PersonTvCast>, Failure>
    suspend fun getPersonDetails(personId: Int): Result<PersonDetails, Failure>
}