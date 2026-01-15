package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito

class HabitosRepository(
    private val local: LocalDatasource,
    // private val remote: RemoteDataSource
) {

    // Obtener todos los hábitos
    fun getAllHabitos() = local.getAllHabitos()

    // Obtener un hábito por ID
    fun getHabitoById(id: Int) = local.getHabitoById(id)

    // Obtener todos los hábitos de un usuario concreto
    fun getHabitosByUserId(userId: Int) = local.getHabitosByUserId(userId)

    // Obtener usuarios con sus hábitos (relación 1-N)
    fun getUsersWithHabitos() = local.getUsersWithHabitos()

    // Insertar o actualizar un hábito
    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)

    // Eliminar un hábito
    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)

    // Para cuando tengamos RemoteDataSource:
    // suspend fun syncHabitos(userId: Int) = remote.syncHabitos(userId)
}
