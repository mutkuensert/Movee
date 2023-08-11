package com.mutkuensert.movee.data.di

import com.mutkuensert.movee.data.account.remote.AccountApi
import com.mutkuensert.movee.data.authentication.AuthenticationApi
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.person.PersonApi
import com.mutkuensert.movee.data.search.MultiSearchApi
import com.mutkuensert.movee.data.search.model.MediaType
import com.mutkuensert.movee.data.search.model.MovieResultItemDto
import com.mutkuensert.movee.data.search.model.MultiSearchResultDto
import com.mutkuensert.movee.data.search.model.PersonResulItemDto
import com.mutkuensert.movee.data.search.model.TvResultItemDto
import com.mutkuensert.movee.data.tvshow.TvShowsApi
import com.mutkuensert.movee.network.AccountIdInterceptor
import com.mutkuensert.movee.network.AuthenticationInterceptor
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
import okhttp3.logging.HttpLoggingInterceptor
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
            PolymorphicJsonAdapterFactory.of(MultiSearchResultDto::class.java, "media_type")
                .withSubtype(MovieResultItemDto::class.java, MediaType.MOVIE)
                .withSubtype(TvResultItemDto::class.java, MediaType.TV)
                .withSubtype(PersonResulItemDto::class.java, MediaType.PERSON)

        return Moshi
            .Builder()
            .add(multiSearchResultFactory)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideClient(accountIdInterceptor: AccountIdInterceptor): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthenticationInterceptor())
            .addInterceptor(accountIdInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
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
    fun provideAuthenticationApi(retrofit: Retrofit): AuthenticationApi {
        return retrofit.create(AuthenticationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAccountApi(retrofit: Retrofit): AccountApi {
        return retrofit.create(AccountApi::class.java)
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