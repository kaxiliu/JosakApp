package edu.josakapp.proyectoJosakapp.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**Instancia de firebase */
object FirebaseClient {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseDatabase = FirebaseDatabase.getInstance()
}