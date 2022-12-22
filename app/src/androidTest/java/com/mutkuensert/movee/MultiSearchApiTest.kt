package com.mutkuensert.movee

import android.util.Log
import com.mutkuensert.movee.data.api.MultiSearchApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TAG = "MultiSearchApiTest"

@HiltAndroidTest
class MultiSearchApiTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var multiSearchApi: MultiSearchApi

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun multiSearchRequestHttpStatusCodeIs200(){
        runTest {
            val response = multiSearchApi.multiSearch("Avengers")
            Assert.assertEquals(200, response.code())
            response.body()?.let { multiSearchModel ->
                Log.i(TAG, "$multiSearchModel")
            }
        }
    }
}