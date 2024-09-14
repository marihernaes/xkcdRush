package com.example.xkcdrush.data.repository

import android.util.Log
import com.example.xkcdrush.data.api.ComicApi
import com.example.xkcdrush.data.model.Comic
import com.google.gson.JsonSyntaxException
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val api: ComicApi
) {

    suspend fun fetchCurrentComic(): Comic? = fetchComic { api.getCurrentComic() }

    suspend fun fetchComicById(id: Int): Comic? = fetchComic { api.getComicById(id) }

    private suspend fun fetchComic(fetcher: suspend () -> Comic): Comic? {
        return try {
            fetcher().let { comic ->
                if (comic.isValid()) {
                    comic
                } else {
                    Log.e("ComicRepository", "Comic data incomplete")
                    null
                }
            }
        } catch (e: JsonSyntaxException) {
            Log.e("ComicRepository", "Error parsing JSON response: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("ComicRepository", "Error fetching comic: ${e.message}", e)
            null
        }
    }
}