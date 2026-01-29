package edu.josakapp.proyectoJosakapp.data.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

/**autenticación*/
class AuthService {
    private val auth = FirebaseClient.auth

    fun login(email: String, pass: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, pass)
    }
    fun getCurrentUser() = auth.currentUser

}