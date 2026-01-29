package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "amigos")
data class Amigo(
    @PrimaryKey val nombre: String
)
