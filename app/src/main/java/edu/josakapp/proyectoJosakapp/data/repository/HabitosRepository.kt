package edu.josakapp.proyectoJosakapp.data.repository


import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import kotlinx.coroutines.flow.Flow


class HabitosRepository(private val local: LocalDatasource,
                        private val userRepository: UserRepository) {


    fun getHabitosByUserId(userId: Int) = local.getHabitosByUserId(userId)


    suspend fun insertHabito(habito: Habito) = local.insertHabito(habito)


    suspend fun updateHabito(habito: Habito) = local.updateHabito(habito)


    suspend fun updateEstado(id: Int, estado: Boolean) = local.updateEstado(id, estado)


    suspend fun deleteHabito(habito: Habito) = local.deleteHabito(habito)




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
