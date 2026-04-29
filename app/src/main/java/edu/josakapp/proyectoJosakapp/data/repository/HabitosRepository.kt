package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.model.toRemote
import kotlinx.coroutines.flow.Flow

class HabitosRepository(private val local: LocalDatasource,
                        private val userRepository: UserRepository) {
                        private val userRepository: UserRepository,
                        private val habitoRemoteRepository: edu.josakapp.proyectoJosakapp.data.remote.HabitoRemoteRepository) {

    fun getHabitosByUserId(userId: Int) = local.getHabitosByUserId(userId)

    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)
    suspend fun insertHabito(habito: Habito): Long {
        val insertedId = local.insertHabito(habito)
        val user = local.getUserById(habito.id_usuario)
        android.util.Log.d("HabitosRepository", "Tratando de sincronizar hábito. UID del usuario: ${user?.uid}")
        if (user != null && user.uid.isNotEmpty()) {
            val habitoRemote = habito.copy(id_habito = insertedId.toInt()).toRemote()
            android.util.Log.d("HabitosRepository", "Enviando hábito a Firestore...")
            habitoRemoteRepository.saveHabito(user.uid, habitoRemote)
        } else {
            android.util.Log.e("HabitosRepository", "No se sincronizó: Usuario es nulo o UID está vacío")
        }
        return insertedId
    }

    suspend fun updateHabito(habito: Habito) = local.updateHabito(habito)
    suspend fun updateHabito(habito: Habito) {
        local.updateHabito(habito)
        val user = local.getUserById(habito.id_usuario)
        if (user != null && user.uid.isNotEmpty()) {
            habitoRemoteRepository.saveHabito(user.uid, habito.toRemote())
        }
    }

    suspend fun updateEstado(id: Int, estado: Boolean) = local.updateEstado(id, estado)
    suspend fun updateEstado(id: Int, estado: Boolean) {
        local.updateEstado(id, estado)
        // Note: For full sync, we'd need to fetch the habit locally, update it, and sync to remote.
    }

    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)
    suspend fun deleteHabito(habito: Habito) {
        local.deleteHabito(habito)
        val user = local.getUserById(habito.id_usuario)
        if (user != null && user.uid.isNotEmpty()) {
            habitoRemoteRepository.deleteHabito(user.uid, habito.id_habito.toString())
        }
    }


    fun getAllRegistros(): Flow<List<HabitoRegistro>> = local.getAllRegistros()
    suspend fun insertRegistro(registro: HabitoRegistro) = local.insertRegistro(registro)

    suspend fun deleteRegistro(idHabito: Int, fecha: Long) = local.deleteRegistro(idHabito, fecha)

    // Método para añadir XP a un usuario
    suspend fun addXpToUser(userId: Int, xp: Int) {
        local.updateUserXp(userId, xp)

        //Actualizar en remoto después de modificar localmente
        val updatedUser = local.getUserById(userId)
        if (updatedUser != null && updatedUser.uid.isNotEmpty()) {
            userRepository.syncUserToRemote(updatedUser)
        }
    }


    suspend fun exchangeXpForMonedas(userId: Int, xpCost: Int, monedasGain: Int) {
        val user = local.getUserById(userId) ?: return
        if (user.xp_total >= xpCost) {
            val updated = user.copy(
                xp_total = user.xp_total - xpCost,
                monedas = user.monedas + monedasGain
            )
            local.insertUser(updated)
        }
    }

}