package com.example.tga.data

import com.example.tga.data.model.Game
import retrofit2.http.GET

interface Api {
    @GET("api1/games")
    suspend fun getGamesList(): List<Game>

    companion object {
        const val BASE_URL = "https://www.mmobomb.com/"
    }
}