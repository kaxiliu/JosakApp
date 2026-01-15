package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
//import edu.josakapp.proyectoJosakapp.data.remote.RemoteDataSource /para cuando tengamos el remote/
import edu.josakapp.proyectoJosakapp.data.model.User

class UserRepository(
    private val local: LocalDatasource,
    //private val remote: RemoteDataSource
) {

    fun getUsersWithHabitos() = local.getUsersWithHabitos()

    suspend fun getUserById(id: Int) = local.getUserById(id)

    suspend fun insertUser(user: User) = local.insertUser(user)

    //suspend fun syncUser(id: Int) = remote.syncUser(id)

    //suspend fun validatePremium(id: Int) = remote.validatePremium(id)
}
