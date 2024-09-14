package com.example.xkcdrush.di

import com.example.xkcdrush.data.api.ComicApi
import com.example.xkcdrush.data.repository.ComicRepository
import com.example.xkcdrush.domain.GetCurrentComicUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://xkcd.com/") // Base URL for the API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideComicApi(retrofit: Retrofit): ComicApi {
        return retrofit.create(ComicApi::class.java)
    }
}