package com.mutkuensert.movee.data.di

import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.person.PersonApi
import com.mutkuensert.movee.data.search.MultiSearchApi
import com.mutkuensert.movee.data.search.model.MediaType
import com.mutkuensert.movee.data.search.model.MovieResultItemModel
import com.mutkuensert.movee.data.search.model.MultiSearchResultMediaType
import com.mutkuensert.movee.data.search.model.PersonResulItemModel
import com.mutkuensert.movee.data.search.model.TvResultItemModel
import com.mutkuensert.movee.data.tvshow.TvShowsApi
import com.mutkuensert.movee.network.ApiKeyInterceptor
import com.mutkuensert.movee.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @GenericMoshi
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
        val multiSearchResultFactory =
            PolymorphicJsonAdapterFactory.of(MultiSearchResultMediaType::class.java, "media_type")
                .withSubtype(MovieResultItemModel::class.java, MediaType.MOVIE)
                .withSubtype(TvResultItemModel::class.java, MediaType.TV)
                .withSubtype(PersonResulItemModel::class.java, MediaType.PERSON)

        return Moshi
            .Builder()
            .add(multiSearchResultFactory)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addNetworkInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@GenericMoshi moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTvShowsApi(retrofit: Retrofit): TvShowsApi {
        return retrofit.create(TvShowsApi::class.java)
    }

    @Singleton
    @Provides
    fun providePersonApi(retrofit: Retrofit): PersonApi {
        return retrofit.create(PersonApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMultiSearchApi(
        @MultiSearchResultMoshi moshi: Moshi,
        client: OkHttpClient
    ): MultiSearchApi {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MultiSearchApi::class.java)
    }
}