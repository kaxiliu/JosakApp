package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: RankingRepository = RankingRepository(),
    private val localDatasource: LocalDatasource
) : ViewModel() {

    private val _ranking = MutableStateFlow<List<UserRanking>>(emptyList())
    val ranking: StateFlow<List<UserRanking>> = _ranking

    private val _soloAmigos = MutableStateFlow(false)
    val soloAmigos: StateFlow<Boolean> = _soloAmigos

    private val _amigos = MutableStateFlow<List<String>>(emptyList())
    val amigos: StateFlow<List<String>> = _amigos

    init {
        // Cargar amigos desde Room
        viewModelScope.launch {
            localDatasource.getAmigos().collect { lista ->
                _amigos.value = lista.map { it.nombre }
                loadRanking()
            }
        }

        // Recargar ranking al cambiar modo
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
