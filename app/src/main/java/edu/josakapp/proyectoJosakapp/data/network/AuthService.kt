package edu.josakapp.proyectoJosakapp.data.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /** Login con email y contraseña */
    fun login(email: String, pass: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, pass)
    }

    /** Registro de usuario */
    fun register(email: String, password: String) =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)


    /** Usuario actual */
    fun getCurrentUser() = auth.currentUser

    /** UID del usuario actual */
    fun getUid(): String? = auth.currentUser?.uid

    /** Saber si hay sesión activa */
    fun isLoggedIn(): Boolean = auth.currentUser != null

    /** Cerrar sesión */
    fun logout() = auth.signOut()
}
