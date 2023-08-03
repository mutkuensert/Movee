package com.mutkuensert.movee.util

class NavConstants {
    class Movie {
        companion object {
            const val ROUTE_MOVIES = "moviesRoute"
            const val ROUTE_MOVIE_DETAILS = "movieDetailsRoute"
            const val GRAPH_MOVIE = "movieNavGraph"
        }
    }

    class TvShow {
        companion object {
            const val ROUTE_TV_SHOWS = "tvShowsRoute"
            const val ROUTE_TV_SHOW_DETAILS = "tvShowDetailsRoute"
            const val GRAPH_TV_SHOWS = "tvShowsNavGraph"
        }
    }

    class Person {
        companion object {
            const val ROUTE_PERSON = "personRoute"
        }
    }

    class Search {
        companion object {
            const val ROUTE_MULTI_SEARCH = "multiSearchRoute"
        }
    }

    class Login {
        companion object {
            const val KEY_IS_REDIRECTED_FROM_TMDB_LOGIN = "isRedirectedFromTmdbLogin"

            const val ROUTE_LOGIN = "loginRoute/{$KEY_IS_REDIRECTED_FROM_TMDB_LOGIN}"

            fun getRouteLoginWithEntity(isRedirectedFromTmdbLogin: Boolean) =
                ROUTE_LOGIN.replace(
                    KEY_IS_REDIRECTED_FROM_TMDB_LOGIN,
                    isRedirectedFromTmdbLogin.toString()
                )
        }
    }
}


const val BASE_URL = "https://api.themoviedb.org/3/"

const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

const val POSTER_SIZE_W500 = "w500"

const val SIZE_ORIGINAL = "original"

const val APP_DEEP_LINK = "mutkuensert.movee://app/"