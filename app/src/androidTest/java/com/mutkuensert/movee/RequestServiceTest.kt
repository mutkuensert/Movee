package com.mutkuensert.movee

import android.util.Log
import com.mutkuensert.movee.data.source.RequestService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TAG = "RequestServiceTest"

@HiltAndroidTest
class RequestServiceTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestService: RequestService

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun requestPopularMovies(){
        runTest {
            val response = requestService.getPopularMovies()
            Assert.assertEquals(200, response.code())
            response.body()?.let { popularMovies ->
                Log.i(TAG, "${popularMovies.results}")
            }
        }
    }
}