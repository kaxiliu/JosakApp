package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.network.AuthService
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO(),
            database.amigosDAO()
        )

        userRepository = UserRepository(localDatasource, AuthService())
    }

    fun getUsersWithHabitos() = userRepository.getUsersWithHabitos()

    suspend fun getUserById(id: Int) = userRepository.getUserById(id)

    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    /**Función que se usara en el mainActivity para el logeo del user*/
    fun isUserLogged(): Boolean {
        return userRepository.isUserLogged()
    }
}
