package com.mutkuensert.movee.data.person

import com.mutkuensert.movee.data.person.model.PersonDetailsResponse
import com.mutkuensert.movee.data.person.model.PersonMovieCreditsResponse
import com.mutkuensert.movee.data.person.model.PersonTvCreditsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int
    ): Response<PersonDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int
    ): Response<PersonMovieCreditsResponse>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPersonTvCredits(
        @Path("person_id") personId: Int
    ): Response<PersonTvCreditsResponse>
}