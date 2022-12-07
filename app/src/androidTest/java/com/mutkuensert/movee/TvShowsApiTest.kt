package com.mutkuensert.movee

import android.util.Log
import com.mutkuensert.movee.data.api.TvShowsApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TAG = "TvShowsApiTest"

@HiltAndroidTest
class TvShowsApiTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var tvShowsApi: TvShowsApi

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun popularTvShowsRequestHttpStatusCodeIs200() {
        runTest {
            val response = tvShowsApi.getPopularTvShows()
            Assert.assertEquals(200, response.code())

            response.body()?.let { popularTvShows ->
                Log.i(TAG, popularTvShows.results.size.toString())
                Log.i(TAG, popularTvShows.results.toString())
            }
        }
    }

    @Test
    fun topRatedTvShowsRequestHttpStatusCodeIs200() {
        runTest {
            val response = tvShowsApi.getTopRatedTvShows()
            Assert.assertEquals(200, response.code())

            response.body()?.let { topRatedTvShows ->
                Log.i(TAG, topRatedTvShows.results.size.toString())
                Log.i(TAG, topRatedTvShows.results.toString())
            }
        }
    }
}