package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.network.AuthService

class UserRepository(
    private val local: LocalDatasource,
    // private val remote: RemoteDataSource
    private val authService: AuthService
) {

    // Obtener usuarios con sus hábitos
    fun getUsersWithHabitos() = local.getUsersWithHabitos()

    // Obtener usuario por ID
    suspend fun getUserById(id: Int) = local.getUserById(id)

    // Insertar o actualizar usuario
    suspend fun insertUser(user: User) = local.insertUser(user)

    fun isUserLogged(): Boolean {
        return authService.getCurrentUser() != null
    }



    // Para cuando tengamos RemoteDataSource:
    // suspend fun syncUser(id: Int) = remote.syncUser(id)
    // suspend fun validatePremium(id: Int) = remote.validatePremium(id)
}
