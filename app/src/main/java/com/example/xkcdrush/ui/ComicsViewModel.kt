package com.example.xkcdrush.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xkcdrush.data.model.Comic
import com.example.xkcdrush.domain.GetComicByIdUseCase
import com.example.xkcdrush.domain.GetCurrentComicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

sealed class ComicState {
    data object Loading : ComicState()
    data class Success(val comic: Comic) : ComicState()
    data class Error(val message: String) : ComicState()
}

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val getCurrentComicUseCase: GetCurrentComicUseCase,
    private val getComicByIdUseCase: GetComicByIdUseCase,
) : ViewModel() {

    private val _comicState = MutableLiveData<ComicState>()
    val comicState: LiveData<ComicState> = _comicState

    private var currentComicNum: Int? = null

    init {
        loadCurrentComic()
    }

    private fun loadCurrentComic() = loadComic { getCurrentComicUseCase.invoke() }

    fun loadPreviousComic() {
        currentComicNum?.let { id ->
            loadComicById(max(1, id - 1))
        }
    }

    fun loadNextComic() {
        currentComicNum?.let { id ->
            loadComicById(id + 1)
        }
    }

    private fun loadComicById(id: Int) = loadComic { getComicByIdUseCase.invoke(id) }

    private fun loadComic(loadComic: suspend () -> Comic?) {
        _comicState.value = ComicState.Loading
        viewModelScope.launch {
            try {
                val comic = loadComic()
                if (comic == null) {
                    _comicState.value = ComicState.Error("Comic is null")
                } else {
                    _comicState.value = ComicState.Success(comic)
                    currentComicNum = comic.num
                }
            } catch (e: Exception) {
                _comicState.value = ComicState.Error("Failed to load comic: ${e.message}")
            }
        }
    }

}
