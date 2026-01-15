package edu.josakapp.proyectoJosakapp.data.datasource

import edu.josakapp.proyectoJosakapp.data.local.HabitosDao
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.model.UserWithHabito
import kotlinx.coroutines.flow.Flow

class LocalDatasource (private val dao: HabitosDao){
    val currentUsuario: Flow<List<UserWithHabito>> =dao.getUsersWithHabitos()
    val currentHabito:Flow<List<Habito>> =dao.getAllHabitos()

    suspend fun deleteHabito(habito: Habito): Int { return dao.deleteHabito(habito) } // Returns the number of rows deleted.
    suspend fun saveHabito(habito: Habito){ dao.insertHabito(habito) }
    fun getHabitoById(idHabito: Int): Flow<Habito?> = dao.getHabitoById(idHabito)
    suspend fun saveUsuario(user: User){dao.insertUser(user)}
    fun getUserById(idUser: Int): Flow<Habito?> = dao.getUserById(idUser)

}