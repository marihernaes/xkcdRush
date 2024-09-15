package com.example.xkcdrush.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.xkcdrush.R
import com.example.xkcdrush.databinding.FragmentComicsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ComicsFragment : Fragment(R.layout.fragment_comics) {

    private lateinit var binding: FragmentComicsBinding
    private val viewModel: ComicsViewModel by viewModels()

    private val defaultCalendar = Calendar.getInstance()
    private val defaultTimeFormat =
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH) // Sunday, October 13, 2024

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentComicsBinding.bind(view)

        binding.loadNextButton.setOnClickListener {
            viewModel.loadNextComic()
        }

        binding.loadPreviousButton.setOnClickListener {
            viewModel.loadPreviousComic()
        }

        binding.loadRandomButton.setOnClickListener {
            viewModel.loadRandomComic()
        }

        binding.loadNewestButton.setOnClickListener {
            viewModel.loadNewestComic()
        }

        viewModel.comicState.observe(viewLifecycleOwner) { comicState ->
            val imageView: ImageView = view.findViewById(R.id.imageView)
            var alt = ""
            var title = ""
            var subtitle = ""

            when (comicState) {
                is ComicState.Error -> {
                    imageView.load(R.drawable.error)
                    updateButtonStates()
                }

                is ComicState.Loading -> {
                    disableButtons()
                    imageView.load(R.drawable.placeholder)
                }

                is ComicState.Success -> {
                    comicState.comic.let { comic ->
                        imageView.load(comic.img) {
                            placeholder(R.drawable.placeholder)
                            error(R.drawable.error)
                        }
                        alt = comic.alt
                        defaultCalendar.set(
                            comic.year.toInt(),
                            comic.month.toInt(),
                            comic.day.toInt()
                        )
                        subtitle = defaultTimeFormat.format(defaultCalendar.time)
                        title = comic.title
                    }
                    updateButtonStates()
                }
            }
            binding.title.text = title
            binding.subtitle.text = subtitle
            binding.imageView.setOnLongClickListener {
                Snackbar.make(binding.imageView, alt, Snackbar.LENGTH_LONG)
                    .setTextMaxLines(20)
                    .show()
                true
            }
        }

        viewModel.loadCurrentComic()
        updateButtonStates()
    }

    private fun updateButtonStates() {
        binding.loadNextButton.isEnabled = !viewModel.isNewestComicDisplayed()
        binding.loadPreviousButton.isEnabled = !viewModel.isOldestComicDisplayed()
        binding.loadRandomButton.isEnabled = true
    }

    private fun disableButtons() {
        binding.loadNextButton.isEnabled = false
        binding.loadPreviousButton.isEnabled = false
        binding.loadRandomButton.isEnabled = false
    }
}
