package edu.josakapp.proyectoJosakapp.data.repository

import android.util.Log
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.*
import edu.josakapp.proyectoJosakapp.data.network.AuthService
import edu.josakapp.proyectoJosakapp.data.remote.UserRemoteRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val local: LocalDatasource,
    private val authService: AuthService,
    private val remote: UserRemoteRepository,
    private val habitoRemoteRepository: edu.josakapp.proyectoJosakapp.data.remote.HabitoRemoteRepository
) {

    suspend fun loadUser(uid: String): User {

        //obtener el usuario desde la base de datos local
        val existingLocal = local.getUserByUid(uid)

        // obtener desde Firestore
        val remoteUser = remote.getUser(uid)

        return if (remoteUser != null) {

            val localUser = remoteUser.toLocal(existingId = existingLocal?.id_usuario ?: 0)
            val generatedId = local.insertUser(localUser)
            // Actualizar el objeto con el ID real generado por Room si era nuevo
            val finalUser = if (localUser.id_usuario == 0) localUser.copy(id_usuario = generatedId.toInt()) else localUser
            
            // Sincronizar hábitos desde Firestore a la base de datos local
            val remoteHabitos = habitoRemoteRepository.getHabitos(uid)
            remoteHabitos.forEach { habitoRemote ->
                local.insertHabito(habitoRemote.toLocal(finalUser.id_usuario))
            }

            finalUser
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
            val generatedId = local.insertUser(localUser)
            localUser.copy(id_usuario = generatedId.toInt())
        }
    }

    suspend fun createUser(uid: String, name: String, email: String): User {
        val newUser = User(
            uid = uid,
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
        val generatedId = local.insertUser(newUser)

        return newUser.copy(id_usuario = generatedId.toInt())
    }

    // Sincronizar un usuario local con Firestore
    suspend fun syncUserToRemote(user: User) {
        val uid = user.uid
        if (uid.isNotEmpty()) {
            remote.saveUser(user.toRemote(uid))
        }
    }

    /**Funcion para la collecion social que cree en Firestore*/
    suspend fun followUser(myId: String, targetId: String) {
        try {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

            val relacionId = "${myId}_${targetId}"

            val datos = hashMapOf(
                "followerId" to myId,
                "followingId" to targetId,
                "timestamp" to FieldValue.serverTimestamp() // <--- El que te daba error
            )

            db.collection("social")
                .document(relacionId)
                .set(datos)

            android.util.Log.d("FIREBASE_SOCIAL", "Relación creada: $relacionId")
        } catch (e: Exception) {
            android.util.Log.e("FIREBASE_SOCIAL", "Error al guardar relación: ${e.message}")
        }
    }

    suspend fun addFriendLocal(nombre: String) {
        local.insertAmigo(nombre)
    }

    suspend fun getLocalFriendNames(): Set<String> {
        return local.getAmigos().first().map { it.nombre }.toSet()
    }

    suspend fun getFollowersCount(userId: String): Int {
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        return try {
            val query = db.collection("social")
                .whereEqualTo("followingId", userId)
                .get()
                .await()

            query.size()
        } catch (e: Exception) {
            Log.e("SOCIAL_DEBUG", "Error al contar seguidores: ${e.message}")
            0
        }
    }
    suspend fun getFollowingCount(userId: String): Int {
        val db = FirebaseFirestore.getInstance()
        val snapshot = db.collection("social")
            .whereEqualTo("followerId", userId)
            .get().await()
        return snapshot.size()
    }

    suspend fun searchUsers(query: String): List<User> {
        val db = FirebaseFirestore.getInstance()
        return try {
            if (query.isEmpty()) {
                Log.d("FIREBASE_SEARCH", "Query vacío, retornando lista vacía")
                return emptyList()
            }

            val snapshot = db.collection("users")
                .whereGreaterThanOrEqualTo("nombre_usuario", query)
                .whereLessThanOrEqualTo("nombre_usuario", query + "\uf8ff")
                .limit(50)
                .get()
                .await()

            Log.d("FIREBASE_SEARCH", "Usuarios encontrados: ${snapshot.size()}")

            snapshot.documents.mapNotNull { doc ->
                try {
                    User(
                        uid = doc.getString("uid") ?: "",
                        nombre_usuario = doc.getString("nombre_usuario") ?: "Sin nombre",
                        email = doc.getString("email") ?: "",
                        contrasena = doc.getString("contrasena") ?: "",
                        esPremium = doc.getBoolean("esPremium") ?: false,
                        monedas = doc.getLong("monedas")?.toInt() ?: 0,
                        fecha_registro = doc.getLong("fecha_registro")?.toLong() ?: 0,
                        xp_total = doc.getLong("xp_total")?.toInt() ?: 0,
                        telefono = doc.getLong("telefono")?.toInt() ?: 0,
                        fotoPerfil = doc.getString("fotoPerfil") ?: "",
                        nivel = doc.getLong("nivel")?.toInt() ?: 0,
                        puntos = doc.getLong("puntos")?.toInt() ?: 0,
                        id_usuario = doc.getLong("id_usuario")?.toInt() ?: 0
                    )
                } catch (e: Exception) {
                    Log.e("FIREBASE_SEARCH", "Error mapeando usuario: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FIREBASE_SEARCH", "Error en búsqueda: ${e.message}")
            emptyList()
        }
    }

    fun isUserLogged(): Boolean = authService.getCurrentUser() != null

    fun getUsersWithHabitos() = local.getUsersWithHabitos()

    suspend fun getUserById(id: Int) = local.getUserById(id)

    suspend fun insertUser(user: User): Long = local.insertUser(user)
}
