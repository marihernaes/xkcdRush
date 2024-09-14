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
object AppModule {

    @Provides
    @Singleton
    fun provideComicRepository(api: ComicApi): ComicRepository {
        return ComicRepository(api)
    }

    @Provides
    @Singleton
    fun provideGetCurrentComicUseCase(repository: ComicRepository): GetCurrentComicUseCase {
        return GetCurrentComicUseCase(repository)
    }
}