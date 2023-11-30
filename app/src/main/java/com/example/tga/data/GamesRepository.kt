package com.example.tga.data

import com.example.tga.data.model.Game
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    suspend fun getGamesList(): Flow<Result<List<Game>>>
}