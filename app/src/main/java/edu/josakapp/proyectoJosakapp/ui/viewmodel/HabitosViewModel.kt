package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    var name by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        name = newName
    }

    private val _habitos = MutableStateFlow<List<Habito>>(emptyList())
    val habitos: StateFlow<List<Habito>> = _habitos.asStateFlow()

    private val localDatasource: LocalDatasource

    private val userRepository: UserRepository
    private val habitosRepository: HabitosRepository

    init {
        val database = AppDatabase.getInstance(application)

        val userDao = database.usersDAO()
        val habitosDao = database.habitosDAO()
        val amigosDao = database.amigosDAO()   // ← AÑADIDO

        // Ahora sí: pasamos los 3 DAOs
        localDatasource = LocalDatasource(
            userDao = userDao,
            habitosDao = habitosDao,
            amigosDao = amigosDao
        )

        userRepository = UserRepository(localDatasource)
        habitosRepository = HabitosRepository(localDatasource)
    }
}
