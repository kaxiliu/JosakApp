package edu.josakapp.proyectoJosakapp.data.remote

import edu.josakapp.proyectoJosakapp.data.model.User
import retrofit2.http.*

interface UserApi {

    @GET("users/{uid}.json")
    suspend fun getUser(@Path("uid") uid: String): User?

    @PUT("users/{uid}.json")
    suspend fun saveUser(@Path("uid") uid: String, @Body user: User): User
}
