package edu.josakapp.proyectoJosakapp.data.datasource

import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.data.remote.RetrofitClient

class RankingRemoteDataSource {

    suspend fun getRanking(): List<UserRanking> {
        return try {
            RetrofitClient.api.getRanking()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
