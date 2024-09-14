package com.example.xkcdrush.domain

import com.example.xkcdrush.data.model.Comic
import com.example.xkcdrush.data.repository.ComicRepository
import javax.inject.Inject

class GetComicByIdUseCase @Inject constructor(
    private val repository: ComicRepository
) {
    suspend operator fun invoke(id: Int): Comic? {
        return repository.fetchComicById(id)
    }
}