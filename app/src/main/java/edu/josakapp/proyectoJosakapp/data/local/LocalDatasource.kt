package edu.josakapp.proyectoJosakapp.data.local

import edu.josakapp.proyectoJosakapp.data.model.Amigo
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.model.User
import kotlinx.coroutines.flow.Flow

class LocalDatasource(
    private val userDao: UserDao,
    private val habitosDao: HabitosDao,
    private val amigosDao: AmigosDao   // ✅ AÑADIDO
) {

    val currenthabitos: Flow<List<Habito>> = habitosDao.getAllHabitos()

    // -------------------------
    // USER DAO
    // -------------------------

    fun getUsersWithHabitos() = userDao.getUsersWithHabitos()

    suspend fun getUserById(id: Int) = userDao.getUserById(id)

    suspend fun insertUser(user: User) = userDao.insertUser(user)


    // -------------------------
    // HABITOS DAO
    // -------------------------

    fun getAllHabitos() = habitosDao.getAllHabitos()

    suspend fun getHabitoById(id: Int): Habito? = habitosDao.getHabitoById(id)

    fun getHabitosByUserId(userId: Int) = habitosDao.getHabitosByUserId(userId)

    suspend fun insertHabito(habito: Habito): Long {
        return habitosDao.insertHabito(habito)
    }

    suspend fun deleteHabito(habito: Habito) = habitosDao.deleteHabito(habito)

    suspend fun updateEstado(id: Int, estado: Boolean) {
        habitosDao.updateEstado(id, estado)
    }

    suspend fun updateHabito(habito: Habito): Int {
        return habitosDao.updateHabito(habito)
    }

    fun getAllRegistros() = habitosDao.getAllRegistros()

    suspend fun insertRegistro(registro: HabitoRegistro) = habitosDao.insertRegistro(registro)

    suspend fun deleteRegistro(idHabito: Int, fecha: Long) = habitosDao.deleteRegistro(idHabito, fecha)



    // -------------------------
    // AMIGOS DAO  ✅ NUEVO
    // -------------------------

    fun getAmigos() = amigosDao.getAmigos()

    suspend fun insertAmigo(nombre: String) =
        amigosDao.insertAmigo(Amigo(nombre))
}
