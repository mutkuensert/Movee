package com.mutkuensert.movee.di

import com.mutkuensert.movee.data.api.MovieApi
import com.mutkuensert.movee.data.api.PersonApi
import com.mutkuensert.movee.data.api.MultiSearchApi
import com.mutkuensert.movee.data.api.TvShowsApi
import com.mutkuensert.movee.data.model.remote.search.*
import com.mutkuensert.movee.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @GeneralMoshi
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @MultiSearchResultMoshi
    @Provides
    fun provideMoshiWithMultiSearchResultFactory(): Moshi {

        val multiSearchResultFactory = PolymorphicJsonAdapterFactory.of(MultiSearchResultMediaType::class.java, "media_type")
            .withSubtype(MovieResultItemModel::class.java, MediaType.movie)
            .withSubtype(TvResultItemModel::class.java, MediaType.tv)
            .withSubtype(PersonResulItemModel::class.java, MediaType.person)

        return Moshi
            .Builder()
            .add(multiSearchResultFactory)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(@GeneralMoshi moshi: Moshi): MovieApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTvShowsApi(@GeneralMoshi moshi: Moshi): TvShowsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TvShowsApi::class.java)
    }

    @Singleton
    @Provides
    fun providePersonApi(@GeneralMoshi moshi: Moshi): PersonApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PersonApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMultiSearchApi(@MultiSearchResultMoshi moshi: Moshi): MultiSearchApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MultiSearchApi::class.java)
    }
}