package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import kotlinx.coroutines.flow.Flow

class HabitosRepository(private val local: LocalDatasource) {

    fun getHabitosByUserId(userId: Int) = local.getHabitosByUserId(userId)

    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)

    suspend fun updateHabito(habito: Habito) = local.updateHabito(habito)

    suspend fun updateEstado(id: Int, estado: Boolean) = local.updateEstado(id, estado)

    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)


    fun getAllRegistros(): Flow<List<HabitoRegistro>> = local.getAllRegistros()
    suspend fun insertRegistro(registro: HabitoRegistro) = local.insertRegistro(registro)

    suspend fun deleteRegistro(idHabito: Int, fecha: Long) = local.deleteRegistro(idHabito, fecha)
}