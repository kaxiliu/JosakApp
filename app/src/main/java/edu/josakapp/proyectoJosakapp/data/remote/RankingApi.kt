package edu.josakapp.proyectoJosakapp.data.remote

import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import retrofit2.http.GET

interface RankingApi {
    @GET("Ranking.json")
    suspend fun getRanking(): List<UserRanking>
}
