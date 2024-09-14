package com.example.xkcdrush.domain

import com.example.xkcdrush.data.model.Comic
import com.example.xkcdrush.data.repository.ComicRepository
import javax.inject.Inject

class GetCurrentComicUseCase @Inject constructor(
    private val repository: ComicRepository
) {
    suspend operator fun invoke(): Comic {
        return repository.fetchCurrentComic()
    }
}