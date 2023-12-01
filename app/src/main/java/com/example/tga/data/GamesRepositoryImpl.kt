package com.example.tga.data

import com.example.tga.data.model.Game
import com.example.tga.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GamesRepositoryImpl(
 private val api: Api
): GamesRepository {

 override suspend fun getGamesList(): Flow<Result<List<Game>>> {
  return flow {
   val gamesFromApi = try {
       api.getGamesList()

   } catch (e: IOException) {
    e.printStackTrace()
    emit(Result.Error(message = "Games loading failed :("))
    return@flow
   } catch (e: HttpException){
    e.printStackTrace()
    emit(Result.Error(message = "Network error happened :("))
    return@flow
   } catch (e: Exception) {
    e.printStackTrace()
    emit(Result.Error(message = "Something went wrong :("))
    return@flow
   }
   emit(Result.Success(gamesFromApi))
  }
 }

}