package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import kotlinx.coroutines.launch

class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    private val habitosRepository: HabitosRepository

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO()
        )
        habitosRepository = HabitosRepository(localDatasource)
    }

    fun getAllHabitos() = habitosRepository.getAllHabitos()

    fun getHabitoById(id: Int) = habitosRepository.getHabitoById(id)

    fun getHabitosByUserId(userId: Int) = habitosRepository.getHabitosByUserId(userId)

    fun insertHabito(habito: Habito) = viewModelScope.launch {
        habitosRepository.insertHabito(habito)
    }

    fun deleteHabito(habito: Habito) = viewModelScope.launch {
        habitosRepository.deleteHabito(habito)
    }
}
