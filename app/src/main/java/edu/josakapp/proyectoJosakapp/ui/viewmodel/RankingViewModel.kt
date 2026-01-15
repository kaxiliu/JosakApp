package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val rankingRepository: RankingRepository

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO()
        )
        rankingRepository = RankingRepository(localDatasource)
    }



    fun getGlobalRanking() = rankingRepository.getGlobalRanking()

    fun getFriendsRanking(userId: Int) = rankingRepository.getFriendsRanking(userId)

    fun updateUserScore(userId: Int, score: Int) = viewModelScope.launch {
        rankingRepository.updateUserScore(userId, score)
    }
}
