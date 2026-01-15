package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val habitosRepository: HabitosRepository

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO()
        )

        userRepository = UserRepository(localDatasource)
        habitosRepository = HabitosRepository(localDatasource)
    }



    fun getUsersWithHabitos() = userRepository.getUsersWithHabitos()

    fun getHabitosByUserId(userId: Int) = habitosRepository.getHabitosByUserId(userId)

    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    fun insertHabito(habito: Habito) = viewModelScope.launch {
        habitosRepository.insertHabito(habito)
    }
}
