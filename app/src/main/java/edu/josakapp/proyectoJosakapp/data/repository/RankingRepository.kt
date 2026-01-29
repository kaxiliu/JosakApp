package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.datasource.RankingRemoteDataSource

class RankingRepository(
    private val remote: RankingRemoteDataSource = RankingRemoteDataSource()
) {
    suspend fun getRanking() = remote.getRanking()
}
