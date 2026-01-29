package edu.josakapp.proyectoJosakapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://raw.githubusercontent.com/saletdiaz/JosakApp/master/app/"

    val api: RankingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RankingApi::class.java)
    }
}
