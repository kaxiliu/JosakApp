package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.data.model.toLocal
import edu.josakapp.proyectoJosakapp.data.remote.UserRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val localDatasource: LocalDatasource
    private val remoteUserRepository = UserRemoteRepository()

    private val _ranking = MutableStateFlow<List<UserRanking>>(emptyList())
    val ranking: StateFlow<List<UserRanking>> = _ranking

    private val _soloAmigos = MutableStateFlow(false)
    val soloAmigos: StateFlow<Boolean> = _soloAmigos

    private val _amigos = MutableStateFlow<List<String>>(emptyList())
    val amigos: StateFlow<List<String>> = _amigos

    private val _usersCache = MutableStateFlow<List<User>>(emptyList())

    init {
        val database = AppDatabase.getInstance(application)
        localDatasource = LocalDatasource(
            database.usersDAO(),
            database.habitosDAO(),
            database.amigosDAO(),
            database.pinguinoDAO()
        )

        // Escuchar cambios en la lista de amigos y usuarios locales
        viewModelScope.launch {
            localDatasource.getAmigos().collect { lista ->
                _amigos.value = lista.map { it.nombre }
            }
        }

        // Escuchar cambios en los usuarios locales y actualizar ranking automáticamente
        viewModelScope.launch {
            localDatasource.getUsersWithHabitos().collect { usersWithHabitos ->
                _usersCache.value = usersWithHabitos.map { it.user }
                recomputeRanking()
            }
        }

        // Sincronizar usuarios reales de Firestore a la base local para que aparezcan todos en el ranking
        viewModelScope.launch {
            syncRemoteUsersToLocal()
            recomputeRanking()
        }
    }

    fun toggleRanking() {
        _soloAmigos.value = !_soloAmigos.value
        recomputeRanking()
    }

    fun addFriend(nombre: String) {
        viewModelScope.launch {
            localDatasource.insertAmigo(nombre)
        }
    }

    fun loadRanking() {
        viewModelScope.launch {
            try {
                syncRemoteUsersToLocal()
                recomputeRanking()
            } catch (e: Exception) {
                recomputeRanking()
            }
        }
    }

    private suspend fun syncRemoteUsersToLocal() {
        val remoteUsers = remoteUserRepository.getAllUsers()
        remoteUsers.forEach { remoteUser ->
            val existingLocal = localDatasource.getUserByUid(remoteUser.uid)
            val localUser = remoteUser.toLocal(existingId = existingLocal?.id_usuario ?: 0)
            localDatasource.insertUser(localUser)
        }
    }

    private fun recomputeRanking() {
        val lista = _usersCache.value.sortedByDescending { it.puntos }
        _ranking.value = if (_soloAmigos.value) {
            lista.filter { user -> _amigos.value.contains(user.nombre_usuario) }
                .map { UserRanking(it.id_usuario, it.nombre_usuario, it.puntos, it.nivel, it.fotoPerfil) }
        } else {
            lista.map { UserRanking(it.id_usuario, it.nombre_usuario, it.puntos, it.nivel, it.fotoPerfil) }
        }
    }
}
