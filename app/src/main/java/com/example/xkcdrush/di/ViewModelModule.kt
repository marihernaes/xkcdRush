package com.example.xkcdrush.di

import com.example.xkcdrush.domain.GetComicByIdUseCase
import com.example.xkcdrush.domain.GetCurrentComicUseCase
import com.example.xkcdrush.ui.ComicsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    fun provideComicsViewModel(
        getCurrentComicUseCase: GetCurrentComicUseCase,
        getComicByIdUseCase: GetComicByIdUseCase,
    ): ComicsViewModel {
        return ComicsViewModel(getCurrentComicUseCase, getComicByIdUseCase)
    }
}