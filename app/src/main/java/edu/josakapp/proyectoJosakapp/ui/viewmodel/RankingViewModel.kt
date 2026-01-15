package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val localDatasource: LocalDatasource

    private val userRepository: UserRepository
    private val habitosRepository: HabitosRepository

    private val rankingRepository: RankingRepository


    init {
        val database = AppDatabase.getInstance(application)
        val userDao = database.usersDAO()
        val habitosDao = database.habitosDAO()
        localDatasource = LocalDatasource(userDao, habitosDao)
        userRepository = UserRepository(localDatasource)
        habitosRepository = HabitosRepository(localDatasource)
        rankingRepository = RankingRepository(localDatasource)
    }
}