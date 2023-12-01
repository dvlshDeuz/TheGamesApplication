package com.example.tga.data

import com.example.tga.data.model.Game
import com.example.tga.data.model.Result
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    suspend fun getGamesList(): Flow<Result<List<Game>>>
}