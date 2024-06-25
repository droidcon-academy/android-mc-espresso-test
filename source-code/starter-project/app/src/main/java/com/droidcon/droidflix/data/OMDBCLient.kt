package com.droidcon.droidflix.data

import com.droidcon.droidflix.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import com.droidcon.droidflix.data.model.Flix
import com.droidcon.droidflix.data.model.OMDBResponse

object OMDBClient {

    private val omdbClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun searchFlix(input: String = "", page: Int = 1): OMDBResponse {
        val search = if (input.isNotBlank()) "s=${input.trim().replace(" ", "_")}" else ""
        val response: HttpResponse = omdbClient.get { url("https://www.omdbapi.com/?apikey=${BuildConfig.API_KEY}&${search}&page=${page}") }
        return response.receive<OMDBResponse>()
    }

    suspend fun searchFlix(id: String): Flix {
        val response: HttpResponse = omdbClient.get { url("https://www.omdbapi.com/?apikey=${BuildConfig.API_KEY}&i=${id}") }
        return response.receive<Flix>()
    }
}