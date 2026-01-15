package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
//import edu.josakapp.proyectoJosakapp.data.remote.RemoteDataSource
import edu.josakapp.proyectoJosakapp.data.model.Habito

class HabitosRepository(
    private val local: LocalDatasource,
    //private val remote: RemoteDataSource
) {

    fun getAllHabitos() = local.getAllHabitos()

    fun getHabitoById(id: Int) = local.getHabitoById(id)

    fun getHabitosByUserId(id: Int) = local.getHabitosByUserId(id)

    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)

    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)

    //suspend fun syncHabitos(userId: Int) = remote.syncHabitos(userId)
}
