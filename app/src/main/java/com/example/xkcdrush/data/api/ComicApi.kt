package com.example.xkcdrush.data.api

import com.example.xkcdrush.data.model.Comic
import retrofit2.http.GET

interface ComicApi {
    @GET("info.0.json")
    suspend fun getCurrentComic(): Comic
}
