package edu.josakapp.proyectoJosakapp.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.josakapp.proyectoJosakapp.data.model.UserRemote
import kotlinx.coroutines.tasks.await

class UserRemoteRepository {

    private val usersRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun getUser(uid: String): UserRemote? {
        val snapshot = usersRef.document(uid).get().await()
        return snapshot.toObject(UserRemote::class.java)
    }

    suspend fun getAllUsers(): List<UserRemote> {
        return try {
            usersRef.get().await().toObjects(UserRemote::class.java)
        } catch (e: Exception) {
            Log.e("UserRemoteRepository", "Error obteniendo todos los usuarios", e)
            emptyList()
        }
    }

    suspend fun saveUser(user: UserRemote) {
        usersRef.document(user.uid).set(user, SetOptions.merge()).await()
    }
}
