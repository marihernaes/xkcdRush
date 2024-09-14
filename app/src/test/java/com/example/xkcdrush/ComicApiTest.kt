package com.example.xkcdrush

import com.example.xkcdrush.data.api.ComicApi
import com.example.xkcdrush.data.model.Comic
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.charset.Charset

class ComicApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var comicApi: ComicApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        comicApi = retrofit.create(ComicApi::class.java)
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test getCurrentComic 2984 returns expected comic 2984`() = runTest {
        val jsonFile = File("src/test/java/com/example/xkcdrush/resources/xkcdValid2984.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(jsonFile.readText(Charset.defaultCharset()))
        mockWebServer.enqueue(mockResponse)

        val actualComic = comicApi.getCurrentComic()

        val expectedComic = Comic(
            month = "9",
            num = 2984,
            year = "2024",
            alt = "Their calculations show it will 'pass within the distance of the moon' but that it 'will not hit the moon, so what's the point?'",
            img = "https://imgs.xkcd.com/comics/asteroid_news.png",
            title = "Asteroid News",
            day = "11"
        )

        assertEquals(expectedComic, actualComic)
    }

    @Test
    fun `test getCurrentComic 614 does not return expected comic 2984`() = runTest {
        val jsonFile = File("src/test/java/com/example/xkcdrush/resources/xkcdValid614.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(jsonFile.readText(Charset.defaultCharset()))
        mockWebServer.enqueue(mockResponse)

        val actualComic = comicApi.getCurrentComic()

        val expectedComic = Comic(
            month = "9",
            num = 2984,
            year = "2024",
            alt = "Their calculations show it will 'pass within the distance of the moon' but that it 'will not hit the moon, so what's the point?'",
            img = "https://imgs.xkcd.com/comics/asteroid_news.png",
            title = "Asteroid News",
            day = "11"
        )

        assertNotEquals(expectedComic, actualComic)
    }

    @Test
    fun `test getCurrentComic returns smaller model for incomplete response`() = runTest {
        val jsonFile =
            File("src/test/java/com/example/xkcdrush/resources/xkcdIncompleteResponse.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(jsonFile.readText(Charset.defaultCharset()))
        mockWebServer.enqueue(mockResponse)

        val actualComic = comicApi.getCurrentComic()

        val expectedComic = Comic(
            month = "7",
            num = 614,
            year = "2009",
            alt = "",
            img = "",
            title = "Woodpecker",
            day = "24"
        )

        assertEquals(expectedComic, actualComic)
    }

    @Test
    fun `fetchCurrentComic handles malformed JSON format`() = runTest {
        val invalidJsonResponse = "{ so invalid. }"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(invalidJsonResponse)
        mockWebServer.enqueue(mockResponse)

        try {
            comicApi.getCurrentComic()
            fail("Expected a JsonSyntaxException to be thrown")
        } catch (e: MalformedJsonException) {
            // Test passes.
        }
    }

    @Test
    fun `fetchComicById returns expected comic for valid ID`() = runTest {
        val validId = 2984
        val jsonFile = File("src/test/java/com/example/xkcdrush/resources/xkcdValid2984.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(jsonFile.readText(Charset.defaultCharset()))
        mockWebServer.enqueue(mockResponse)

        val actualComic = comicApi.getComicById(validId)

        val expectedComic = Comic(
            month = "9",
            num = 2984,
            year = "2024",
            alt = "Their calculations show it will 'pass within the distance of the moon' but that it 'will not hit the moon, so what's the point?'",
            img = "https://imgs.xkcd.com/comics/asteroid_news.png",
            title = "Asteroid News",
            day = "11"
        )

        assertEquals(expectedComic, actualComic)
    }

    @Test
    fun `fetchComicById handles invalid ID`() = runTest {
        val invalidId = 0
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody("{\"error\":\"Comic not found\"}")
        mockWebServer.enqueue(mockResponse)

        try {
            comicApi.getComicById(invalidId)
            fail("Expected an HttpException to be thrown")
        } catch (e: HttpException) {
            assertEquals(404, e.code())
        }
    }
}