package com.example.xkcdrush.data.repository

import com.example.xkcdrush.data.api.ComicApi
import com.example.xkcdrush.data.model.Comic
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val api: ComicApi
) {
    suspend fun fetchCurrentComic(): Comic {
        return api.getCurrentComic()
    }
}