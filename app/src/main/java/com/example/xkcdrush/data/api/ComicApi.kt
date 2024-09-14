package com.example.xkcdrush.data.api

import com.example.xkcdrush.data.model.Comic
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicApi {
    @GET("info.0.json")
    suspend fun getCurrentComic(): Comic

    @GET("{id}/info.0.json")
    suspend fun getComicById(@Path("id") id: Int): Comic
}
