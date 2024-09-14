package com.example.xkcdrush

import com.example.xkcdrush.data.api.ComicApi
import com.example.xkcdrush.data.model.Comic
import com.example.xkcdrush.data.repository.ComicRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ComicRepositoryTest {

    @Mock
    private lateinit var comicApi: ComicApi

    private lateinit var comicRepository: ComicRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        comicRepository = ComicRepository(comicApi)
    }

    @Test
    fun `fetchCurrentComic returns valid comic`() = runTest {
        val validComic = Comic(
            num = 2984,
            title = "Asteroid News",
            img = "https://imgs.xkcd.com/comics/asteroid_news.png",
            alt = "Their calculations show it will 'pass within the distance of the moon' but that it 'will not hit the moon, so what's the point?'",
            month = "9",
            year = "2024",
            day = "11"
        )
        `when`(comicApi.getCurrentComic()).thenReturn(validComic)
        val result = comicRepository.fetchCurrentComic()
        assertEquals(validComic, result)
    }
}