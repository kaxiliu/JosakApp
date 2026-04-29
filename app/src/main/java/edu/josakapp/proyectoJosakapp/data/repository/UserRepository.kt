package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.*
import edu.josakapp.proyectoJosakapp.data.network.AuthService
import edu.josakapp.proyectoJosakapp.data.remote.UserRemoteRepository

class UserRepository(
    private val local: LocalDatasource,
    private val authService: AuthService,
    private val remote: UserRemoteRepository
) {

    suspend fun loadUser(uid: String): User {

        //obtener el usuario desde la base de datos local
        val existingLocal = local.getUserByUid(uid)

        // obtener desde Firestore
        val remoteUser = remote.getUser(uid)

        return if (remoteUser != null) {

            val localUser = remoteUser.toLocal(existingId = existingLocal?.id_usuario ?: 0)
            local.insertUser(localUser)
            localUser
        }
        else if (existingLocal != null) {
            existingLocal
        } else {

            // Crear usuario nuevo
            val newRemote = UserRemote(
                uid = uid,
                nombre_usuario = "Nuevo usuario"
            )

            // Guardar en Firestore
            remote.saveUser(newRemote)

            // Guardar en Room
            val localUser = newRemote.toLocal(existingId = 0)
            local.insertUser(localUser)
            localUser
        }
    }

    suspend fun createUser(uid: String, name: String, email: String): User {
        val newUser = User(
            nombre_usuario = name,
            email = email,
            contrasena = "",
            esPremium = false,
            monedas = 0,
            fecha_registro = System.currentTimeMillis(),
            xp_total = 0,
            telefono = 0,
            fotoPerfil = "",
            nivel = 1,
            puntos = 0
        )

        // Guardar en Firestore como UserRemote
        remote.saveUser(newUser.toRemote(uid))

        // Guardar en Room como User
        local.insertUser(newUser)

        return newUser
    }

    // Sincronizar un usuario local con Firestore
    suspend fun syncUserToRemote(user: User) {
        val uid = user.uid
        if (uid.isNotEmpty()) {
            remote.saveUser(user.toRemote(uid))
        }
    }


    fun isUserLogged(): Boolean = authService.getCurrentUser() != null

    fun getUsersWithHabitos() = local.getUsersWithHabitos()

    suspend fun getUserById(id: Int) = local.getUserById(id)

    suspend fun insertUser(user: User) = local.insertUser(user)
}
