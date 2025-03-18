package com.packt.pets

import com.google.common.io.Resources
import com.google.common.truth.Truth.*
import com.packt.pets.data.CataasApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException

/**
 * Created by Tom Buczynski on 17.03.2025.
 */

val requestDispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {

        return if (request.path != null && request.path!!.startsWith("/cats?tags=")) {
            MockResponse().setResponseCode(200).setBody(getJsonResponse("catsresponse.json"))
        } else {
            MockResponse().setResponseCode(404)
        }
    }
}

private fun getJsonResponse(path: String): String {
    val url = Resources.getResource(path)

    return url.readText()
}

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

class WebApiTests {
    private lateinit var webServer: MockWebServer;
    private lateinit var api: CataasApi;

    @Before
    @Throws(IOException::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.dispatcher = requestDispatcher
        webServer.start()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .baseUrl(webServer.url("/"))
            .build()

        api = retrofit.create(CataasApi::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCataasApi() {
        runTest {
            val response = api.fetchCats("cute", 20)

            assertWithMessage("Response must be successful")
                .that(response.isSuccessful).isTrue()

            assertWithMessage("Response body:")
                .that(response.body()).isNotNull()

            assertWithMessage("Response body size:")
                .that(response.body()!!.size).isEqualTo(20)
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        webServer.shutdown()
    }
}