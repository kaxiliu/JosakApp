package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource

class RankingRepository(
    private val local: LocalDatasource
    // private val remote: RemoteDataSource
) {

    // Cuando tengamos RemoteDataSource

    suspend fun getGlobalRanking(): List<Any> {
        // TODO: implementar cuando tengamos RemoteDataSource
        return emptyList()
    }

    suspend fun getFriendsRanking(userId: Int): List<Any> {
        // TODO: implementar cuando tengamos RemoteDataSource
        return emptyList()
    }

    suspend fun updateUserScore(userId: Int, score: Int) {
        // TODO: implementar cuando tengamos RemoteDataSource
    }
}
