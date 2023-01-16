package com.arsildo.prevail.di

import com.arsildo.prevail.data.source.remote.RetroFitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val API_BASE_URL = "https://a.4cdn.org/"
const val MEDIA_BASE_URL = "https://i.4cdn.org/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkService(retrofit: Retrofit): RetroFitService {
        return retrofit.create(RetroFitService::class.java)
    }

}