package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository

class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    private val localDatasource: LocalDatasource

    private val userRepository: UserRepository
    private val habitosRepository: HabitosRepository

    private val rankingRepository: RankingRepository
    var name by mutableStateOf("")
        private set

    fun updateName(value: String) {
        name = value
    }

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