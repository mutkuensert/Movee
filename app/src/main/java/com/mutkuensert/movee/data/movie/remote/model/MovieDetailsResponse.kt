package com.mutkuensert.movee.data.movie.remote.model

import com.squareup.moshi.Json

data class MovieDetailsResponse(
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "belongs_to_collection") val belongsToCollection: Any?,
    val budget: Int,
    val genres: List<MovieGenreDto>,
    val homepage: String?,
    val id: Int,
    @Json(name = "imdb_id") val imdbId: String?,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<MovieProductionCompanyDto>,
    @Json(name = "production_countries") val productionCountries: List<MovieProductionCountryDto>,
    @Json(name = "release_date") val releaseDate: String,
    val revenue: Long,
    val runtime: Int?,
    @Json(name = "spoken_languages") val spokenLanguages: List<MovieSpokenLanguageDto>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
)

data class MovieGenreDto(
    val id: Int,
    val name: String
)

data class MovieProductionCompanyDto(
    val id: Int,
    @Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @Json(name = "origin_country") val originCountry: String
)

data class MovieProductionCountryDto(
    val iso_3166_1: String,
    val name: String
)

data class MovieSpokenLanguageDto(
    val iso_639_1: String,
    val name: String
)