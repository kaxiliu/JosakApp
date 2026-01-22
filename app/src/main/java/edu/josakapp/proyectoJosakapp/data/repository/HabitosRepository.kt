package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
//import edu.josakapp.proyectoJosakapp.data.remote.RemoteDataSource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import kotlinx.coroutines.flow.Flow

class HabitosRepository(private val local: LocalDatasource) {

    fun getAllHabitos() = local.getAllHabitos()

    fun getHabitoById(id: Int) = local.getHabitoById(id)

    fun getHabitosByUserId(id: Int) = local.getHabitosByUserId(id)

    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)

    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)
}