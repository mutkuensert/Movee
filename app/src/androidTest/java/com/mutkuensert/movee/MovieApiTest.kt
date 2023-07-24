package com.mutkuensert.movee

import android.util.Log
import com.mutkuensert.movee.data.movie.MovieApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TAG = "MovieApiTest"

@HiltAndroidTest
class MovieApiTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var movieApi: MovieApi

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun popularMoviesRequestHttpStatusCodeIs200() {
        runTest {
            val response = movieApi.getPopularMovies()
            Assert.assertEquals(200, response.code())
            response.body()?.let { popularMovies ->
                Log.i(TAG, "${popularMovies.results.size}")
                Log.i(TAG, "${popularMovies.results}")
            }
        }
    }

    @Test
    fun getMovieCreditsHttpStatusCodeIs200() {
        runTest {
            val response = movieApi.getMovieCredits(550)
            Assert.assertEquals(200, response.code())
            response.body()?.let {
                Log.i(TAG, response.body().toString())
            }
        }
    }
}