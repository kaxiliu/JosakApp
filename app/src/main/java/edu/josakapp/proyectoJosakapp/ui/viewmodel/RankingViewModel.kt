package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RankingRepository()
    private val localDatasource: LocalDatasource

    private val _ranking = MutableStateFlow<List<UserRanking>>(emptyList())
    val ranking: StateFlow<List<UserRanking>> = _ranking

    private val _soloAmigos = MutableStateFlow(false)
    val soloAmigos: StateFlow<Boolean> = _soloAmigos

    private val _amigos = MutableStateFlow<List<String>>(emptyList())
    val amigos: StateFlow<List<String>> = _amigos

    init {
        val database = AppDatabase.getInstance(application)
        localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO(),
            database.amigosDAO(),
            database.pinguinoDAO()
        )

        viewModelScope.launch {
            localDatasource.getAmigos().collect { lista ->
                _amigos.value = lista.map { it.nombre }
                loadRanking()
            }
        }

        viewModelScope.launch {
            soloAmigos.collect {
                loadRanking()
            }
        }
    }

    fun toggleRanking() {
        _soloAmigos.value = !_soloAmigos.value
    }

    fun addFriend(nombre: String) {
        viewModelScope.launch {
            localDatasource.insertAmigo(nombre)
        }
    }

    fun loadRanking() {
        viewModelScope.launch {
            val lista = repository.getRanking().sortedByDescending { it.puntos }

            _ranking.value = if (soloAmigos.value) {
                lista.filter { user -> amigos.value.contains(user.nombre_usuario) }
            } else {
                lista
            }
        }
    }
}
