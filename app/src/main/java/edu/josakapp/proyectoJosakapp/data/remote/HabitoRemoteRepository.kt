package edu.josakapp.proyectoJosakapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.josakapp.proyectoJosakapp.data.model.HabitoRemote
import kotlinx.coroutines.tasks.await
import android.util.Log

class HabitoRemoteRepository {

    private val db = FirebaseFirestore.getInstance()

    // Obtener todos los hábitos de un usuario desde Firestore
    suspend fun getHabitos(uid: String): List<HabitoRemote> {
        return try {
            val snapshot = db.collection("users")
                .document(uid)
                .collection("habitos")
                .get()
                .await()
            snapshot.toObjects(HabitoRemote::class.java)
        } catch (e: Exception) {
            Log.e("HabitoRemoteRepo", "Error fetching habitos for uid: $uid", e)
            emptyList()
        }
    }

    // Guardar o actualizar un hábito en Firestore
    suspend fun saveHabito(uid: String, habito: HabitoRemote) {
        try {
            android.util.Log.d("HabitoRemoteRepo", "Iniciando guardado en Firestore: users/$uid/habitos/${habito.id_habito}")
            db.collection("users")
                .document(uid)
                .collection("habitos")
                .document(habito.id_habito.toString())
                .set(habito, SetOptions.merge())
                .await()
            android.util.Log.d("HabitoRemoteRepo", "¡Hábito ${habito.id_habito} guardado con ÉXITO en Firestore!")
        } catch (e: Exception) {
            android.util.Log.e("HabitoRemoteRepo", "Error saving habito: ${habito.id_habito}. Motivo: ${e.message}", e)
        }
    }

    // Eliminar un hábito de Firestore
    suspend fun deleteHabito(uid: String, habitoId: String) {
        try {
            db.collection("users")
                .document(uid)
                .collection("habitos")
                .document(habitoId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e("HabitoRemoteRepo", "Error deleting habito: $habitoId", e)
        }
    }
}
