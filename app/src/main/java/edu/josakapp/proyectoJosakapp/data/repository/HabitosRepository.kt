package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito

class HabitosRepository(private val local: LocalDatasource) {

    fun getHabitosByUserId(userId: Int) = local.getHabitosByUserId(userId)

    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)

    suspend fun updateHabito(habito: Habito) = local.updateHabito(habito)

    suspend fun updateEstado(id: Int, estado: Boolean) = local.updateEstado(id, estado)

    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)
}