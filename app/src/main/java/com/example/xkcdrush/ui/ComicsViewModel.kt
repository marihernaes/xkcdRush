package com.example.xkcdrush.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xkcdrush.data.model.Comic
import com.example.xkcdrush.domain.GetCurrentComicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ComicState {
    data object Loading : ComicState()
    data class Success(val comic: Comic) : ComicState()
    data class Error(val message: String) : ComicState()
}

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val getCurrentComicUseCase: GetCurrentComicUseCase
) : ViewModel() {

    private val _comicState = MutableLiveData<ComicState>()
    val comicState: LiveData<ComicState> = _comicState

    init {
        loadCurrentComic()
    }

    fun loadCurrentComic() {
        _comicState.value = ComicState.Loading
        viewModelScope.launch {
            try {
                val comic = getCurrentComicUseCase.invoke()
                if (comic == null) {
                    _comicState.value = ComicState.Error("Comic is null")
                } else {
                    _comicState.value = ComicState.Success(comic)
                }
            } catch (e: Exception) {
                _comicState.value = ComicState.Error("Failed to load comic: ${e.message}")
            }
        }
    }
}
