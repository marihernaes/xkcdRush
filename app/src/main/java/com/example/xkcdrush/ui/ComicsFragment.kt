package com.example.xkcdrush.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.xkcdrush.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsFragment : Fragment(R.layout.fragment_comics) {
    private val viewModel: ComicsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.comicState.observe(viewLifecycleOwner) { comicState ->
            val imageView: ImageView = view.findViewById(R.id.imageView)
            when (comicState) {
                is ComicState.Error ->
                    imageView.load(R.drawable.error)

                is ComicState.Loading ->
                    imageView.load(R.drawable.placeholder)

                is ComicState.Success ->
                    imageView.load(comicState.comic.img) {
                        placeholder(R.drawable.placeholder)
                        error(R.drawable.error)
                    }
            }
        }
        viewModel.loadCurrentComic()
    }
}
