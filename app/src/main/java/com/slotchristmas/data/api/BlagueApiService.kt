package com.slotchristmas.data.api

import com.slotchristmas.data.model.BlagueResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class BlagueApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val modes = listOf("global", "limit", "blondes")//"dark", "dev", "beauf",

    suspend fun getRandomBlague(): BlagueResponse {
        val mode = modes.random()
        return client.get("https://blague-api.vercel.app/api?mode=$mode").body()
    }
}
